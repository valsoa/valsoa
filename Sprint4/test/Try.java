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
        model.add("age",19);
        return model;
    }

}
