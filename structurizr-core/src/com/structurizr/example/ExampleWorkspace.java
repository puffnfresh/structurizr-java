package com.structurizr.example;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is the example workspace created when a user adds a new workspace in Structurizr.
 */
public class ExampleWorkspace {

    private static final String DATABASE_TAG = "Database";

    public static Workspace create(boolean useShapes) {
        Workspace workspace = new Workspace("Example workspace", "This is an example workspace - drag the elements around to organise them on the diagram.");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");
        SoftwareSystem softwareSystem = model.addSoftwareSystem("Software System", "My software system.");
        user.uses(softwareSystem, "Uses");

        Container singlePageApplication = softwareSystem.addContainer("Single Page Application", "Does interesting things.", "Web Browser & JavaScript");
        Container backendForFrontend = softwareSystem.addContainer("Backend For Frontend", "Does interesting things too.", "Apache Tomcat & Java");
        Container database = softwareSystem.addContainer("Database", "Stores interesting data.", "MySQL");
        database.addTags(DATABASE_TAG);

        user.uses(singlePageApplication, "Uses");
        singlePageApplication.uses(backendForFrontend, "Uses", "HTTPS");
        backendForFrontend.uses(database, "Reads from and writes to", "JDBC");

        Component someController = backendForFrontend.addComponent("Some Controller", "A description of some controller.", "Spring MVC RestController");
        Component someService = backendForFrontend.addComponent("Some Service", "A description of some service.", "Spring Bean");
        Component someRepository = backendForFrontend.addComponent("Some Repository", "A description of some repository.", "Spring Data");

        singlePageApplication.uses(someController, "Uses", "JSON/HTTPS");
        someController.uses(someService, "Uses");
        someService.uses(someRepository, "Uses");
        someRepository.uses(database, "Reads to and writes from", "JDBC");

        // create some views
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createContextView(softwareSystem, "A description of this diagram.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.setPaperSize(PaperSize.A5_Landscape);

        ContainerView containerView = viewSet.createContainerView(softwareSystem, "A description of this diagram.");
        containerView.addAllPeople();
        containerView.addAllContainers();
        containerView.setPaperSize(PaperSize.A5_Landscape);

        ComponentView componentView = viewSet.createComponentView(backendForFrontend, "A description of this diagram.");
        componentView.addAllContainers();
        componentView.addAllComponents();
        componentView.setPaperSize(PaperSize.A5_Landscape);

        // tag and style some elements
        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle(Tags.ELEMENT).color("#ffffff").width(650).height(400).fontSize(36);
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd");
        styles.addElementStyle(Tags.CONTAINER).background("#438dd5");
        styles.addElementStyle(Tags.COMPONENT).background("#85bbf0").color("#000000");
        styles.addRelationshipStyle(Tags.RELATIONSHIP).thickness(5).routing(Routing.Direct).fontSize(32).width(400);

        if (useShapes) {
            styles.addElementStyle(Tags.PERSON).background("#08427b").width(550).shape(Shape.Person);
            styles.addElementStyle(DATABASE_TAG).shape(Shape.Cylinder);
        } else {
            styles.addElementStyle(Tags.PERSON).background("#08427b");
        }

        return workspace;
    }

    public static void main(String[] args) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient("https://localhost:9443", "b40d5dc7-d39c-47b2-8b57-1ac768c09f1a", "9c57d278-c27a-4f0a-9a1e-e26f11e7b607");
        structurizrClient.mergeWorkspace(18, create(true));
    }

}