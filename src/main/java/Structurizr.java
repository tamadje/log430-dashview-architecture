import java.io.File;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Decision;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.*;


/**
 * This is a simple example of how to get started with Structurizr for Java.
 */
public class Structurizr {
    
    private static final long WORKSPACE_ID = 54418;
    private static final String API_KEY = "480ea729-4d22-460d-8f99-595200c5d376";
    private static final String API_SECRET = "e2800877-54e6-4ab2-b059-36f909b523a0";

    public static void main(String[] args) throws Exception {
        // a Structurizr workspace is the wrapper for a software architecture model, views and documentation
        Workspace workspace = new Workspace("Getting Started", "This is a model of my software system.");
        Model model = workspace.getModel();

        // add some elements to your software architecture model
        Person pilot = model.addPerson("Pilot", "The driver of the car.");
        SoftwareSystem vehicule = model.addSoftwareSystem("Vehicule", "My software system.");
        
        Person engineer = model.addPerson("Engineer", "A user of my software system.");
        SoftwareSystem courseManagementSystem = model.addSoftwareSystem("Course Management System", "My software system.");
        
        Person optimisationEngineer = model.addPerson("University Optimisation Engineer", "A user of my software system.");
        SoftwareSystem optimisationSystem = model.addSoftwareSystem("University Optimisation System", "My software system.");
        

        pilot.uses(vehicule, "Uses");
        vehicule.uses(courseManagementSystem,"Uses");
        engineer.uses(courseManagementSystem,"Uses");
        courseManagementSystem.uses(optimisationSystem,"Uses");
        optimisationEngineer.uses(optimisationSystem,"Uses");


        
        
        // define some views (the diagrams you would like to see)
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(courseManagementSystem, "SystemContext", "An example of a System Context diagram.");
        contextView.setPaperSize(PaperSize.A5_Landscape);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

     

        // add some styling
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        //styles.addElementStyle(Tags.RELATIONSHIP).color("#Ff0000");
        styles.addRelationshipStyle("API").color("#ff0000");


        Container engineeringUI = courseManagementSystem.addContainer("Engineering User interface", "User interface to control....", "React");
        Container courseManagementSystem_server = courseManagementSystem.addContainer("Course Management Server", "server that provide an API to record vehicule state and update...", "Web server Golang");
        engineer.uses(engineeringUI,"Uses");
        engineeringUI.uses(courseManagementSystem_server,"Throught API xxx");
        vehicule.uses(courseManagementSystem_server,"API vehicule info").addTags("API");;
        courseManagementSystem_server.uses(optimisationSystem,"api optimisation").addTags("API");
        
        ContainerView courseManagementSystemDetailView = views.createContainerView(courseManagementSystem, "Course Management System container view", "An example of a System Context diagram.");
        courseManagementSystemDetailView.setPaperSize(PaperSize.A5_Landscape);
        courseManagementSystemDetailView.add(engineeringUI);
        courseManagementSystemDetailView.add(vehicule);
        courseManagementSystemDetailView.add(courseManagementSystem_server);
        courseManagementSystemDetailView.addNearestNeighbours(engineeringUI);
        courseManagementSystemDetailView.addNearestNeighbours(courseManagementSystem_server);
       


        // add some documentation
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        File documentationRoot = new File("./documentation");
 
        template.addSection(vehicule, "My custom section for pilot description", Format.Markdown, "- This is a test showing the context view");
        template.addSection(vehicule, "My context view1", Format.Markdown, " ![](embed:SystemContext)");
        template.addSection(vehicule, "My context view2", Format.Markdown, " ![](SystemContext)");
        template.addSection(vehicule, "My context view3", Format.Markdown, " ![](#SystemContext)");

        template.addContextSection(courseManagementSystem, new File(documentationRoot, "context.md"));
        template.addFunctionalOverviewSection(courseManagementSystem, new File(documentationRoot, "functional-overview.md"));
        template.addQualityAttributesSection(courseManagementSystem, new File(documentationRoot, "quality-attributes.md"));
        template.addImages(documentationRoot);

        template.addDecisionLogSection(courseManagementSystem, new File(documentationRoot, "decision-log.md"));
        ///
        

        //template.addImages(new File("..."));
       
    //    courseManagementSystem_server.uses(courseManagementSystem,"API optimisation system");

       //courseManagementSystemDetailView.addNearestNeighbours(courseManagementSystem);

       
        uploadWorkspaceToStructurizr(workspace);
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}