package util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.http.HttpResponse;
import java.rmi.server.ServerCloneException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import util.Annotation.*;
import java.lang.annotation.Annotation;


public class FrontController extends HttpServlet {
    private List<Class<?>> listeController;
    private HashMap<String,Mapping> urlMapping = new HashMap<>();

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        String packages = this.getInitParameter("package");
        ServletContext context = getServletContext();
        listeController = Util.allMappingUrls(packages,util.Annotation.AnnotationController.class,context);
        urlMapping =Util.getUrlMapping(listeController);

    }     
    protected Object typage(String paramValue ,String paramName, Class paramType){
        Object o = null ;
        if (paramType == Date.class || paramType == java.sql.Date.class) {
            try {
                o = java.sql.Date.valueOf(paramValue);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid date format for parameter: " + paramName);
            }
        } else if (paramType == int.class) {
            o = Integer.parseInt(paramValue);
        } else if (paramType == double.class) {
            o = Double.parseDouble(paramValue);
        } else if (paramType == boolean.class) {
            o =Boolean.parseBoolean(paramValue);
        } else {
            o = paramValue; 
        }
        return o;
    }
    public Object[] getAllParams(Method method,HttpServletRequest req)throws IllegalArgumentException{
        Parameter[] parametres = method.getParameters();
        Object[] params = new Object[parametres.length];

        for (int i = 0; i < parametres.length; i++) {
            String nameParam ="";
            if(parametres[i].isAnnotationPresent(util.Annotation.AnnotationParameter.class)){
                nameParam=parametres[i].getAnnotation(util.Annotation.AnnotationParameter.class).value();
            }
            else{
                nameParam=parametres[i].getName();
            }

            Class<?> typeParametre = parametres[i].getType();
            if (!typeParametre.isPrimitive() && !typeParametre.equals(String.class)) {
            try {
                Object paramObject = typeParametre.getDeclaredConstructor().newInstance();
                Field[] fields = typeParametre.getDeclaredFields();
                
                for (Field field : fields) {
                    String fieldName = field.getName();
                    String fieldValue = req.getParameter(nameParam + "." + fieldName);
                    if (fieldValue != null) {
                        field.setAccessible(true);
                        Object typedValue = typage(fieldValue, fieldName, field.getType());
                        field.set(paramObject, typedValue);
                    }
                }
                params[i] = paramObject;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalArgumentException("Error creating parameter object: " + nameParam, e);
            }
        } else {
            String paramValue = req.getParameter(nameParam);
            if (paramValue == null) {
                throw new IllegalArgumentException("Missing parameter: " + nameParam);
            }
            params[i] = typage(paramValue, nameParam, typeParametre);
        }
        }
        return params;
    }               

    public Object getValue(Mapping mapping, String className,HttpServletRequest req){
        try{
            Class<?> clas =Class.forName(className);
            Method method =clas.getMethod(mapping.getMethodName(),mapping.getTypes()); 
            Object obj = clas.newInstance();
            Object[] objectParam=getAllParams(method,req);
            return method.invoke(obj,objectParam);
        } 
        catch(Exception e){
            return null;
        }
    }
   
    public void sendModelView(ModelView model,HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException{
        for(Map.Entry<String,Object> entry : model.getData().entrySet()){
            req.setAttribute(entry.getKey(),entry.getValue());
        }
        RequestDispatcher dispatch = req.getRequestDispatcher(model.getUrl());
        dispatch.forward(req,rep);
    }  
    public void executeUrl(HttpServletRequest req, HttpServletResponse resp,boolean test) throws ServletException, IOException {
        resp.getWriter().println("<br>urlMapping:"+urlMapping);
        for (Map.Entry<String,Mapping> entry : urlMapping.entrySet()) {
            String url = entry.getKey();
            Mapping value = entry.getValue();
            if(url.equals(req.getRequestURI())){
                Object urlValue= getValue(value,value.getClassName(),req);
                resp.getWriter().println("<br>valeur:" + value.getClassName() +"_"+ value.getMethodName());
                if(urlValue instanceof String s){
                    resp.getWriter().println("<br>valeur methode:"+s);
                }
                else if(urlValue instanceof ModelView m){
                    sendModelView(m,req,resp);
                }
                else{
                    // resp.getWriter().println("<br>type de retour non valide"); tsy azo ato zaooo
                    throw new IllegalArgumentException("type de retour non valide");
                }
                test=true;
                break;
            }    
        }
        if (!test) {
            throw new IllegalArgumentException("url innexistante");
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         boolean test = false;
        resp.setContentType("text/html");
        resp.getWriter().println("<h1> Hello world!!</h1>");
        resp.getWriter().println("<br><h1>Lien :" + req.getRequestURI() + "</h1>");

        for (Class<?> controllerClass : listeController) {
            AnnotationController annotation = controllerClass.getAnnotation(util.Annotation.AnnotationController.class);
            if (annotation != null) {
                String nameController = controllerClass.getSimpleName();
                resp.getWriter().println("<br>controller :" + nameController);
            } else {
                resp.getWriter().println("<br>Annotation nulle");
            }
        }
       executeUrl(req,resp,test);
    }
}
