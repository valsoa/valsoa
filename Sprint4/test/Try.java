package objet;
import util.Annotation.*;
import util.ModelView;


@AnnotationController
public class Try {
    @Get("/Andrana/save")
    public void save(){

    }
    @Get("/Andrana/modifier")
    public String modifier(){
        return "modifier";
    }
    @Get("/Andrana/liste")
    public ModelView liste (){
        ModelView model =new ModelView("liste.jsp");
        model.add("nom","Valisoa");
        model.add("age",20);
        return model;
    }
    // @Get("/Andrana/number")
    // public int number(){
    //     return 20;
    // }
    @Get("/Andrana/number")
    public int number1(){
        return 20;
    }
    @Get("/Andrana/voirTout")
    public ModelView voirListe(@AnnotationParameter("name")String nom,@AnnotationParameter("age")int age){
        ModelView model =new ModelView("liste.jsp");
        model.add("nom",nom);
        model.add("age",age);
        return model;
    }
    @Get("/Andrana/objetType")
    public ModelView voirPersonne(@AnnotationParameter("objet") Personne personne){
        ModelView model =new ModelView("liste.jsp");
        model.add("nom",personne.getNom());
        model.add("age",personne.getAge());
        return model;
    }
}
