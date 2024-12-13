package org.example.housekeeping_manager;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;

import org.example.housekeeping_manager.rooms.Room;
import org.example.housekeeping_manager.tasks.Task;

import java.util.List;

@Route("")
public class manager_gui extends VerticalLayout {

    private Span totalRoomsText;
    private Span roomsCleanedText;
    private Span roomsNotCleanedText;
    private HorizontalLayout roomLayout;

    public manager_gui() {
        setWidthFull();
        setSpacing(true);
        setPadding(true);

        // Header Section
        H2 title = new H2("Housekeeper Manager");
        Span subtitle = new Span("powered by Nico Köchli - Programming project 2, FHNW");
        Image logo = new Image("fhnw_logo.jpg", "FHNW Logo");
        logo.setHeight("50px");

        HorizontalLayout header = new HorizontalLayout(new VerticalLayout(title, subtitle), logo);
        header.setAlignItems(Alignment.CENTER);
        header.setWidthFull();
        header.getStyle()
                .set("background-color", "#ffffff")
                .set("padding", "20px");

        // Black Separator Line
        Div separator = createSeparator();

        // Tabs Navigation Bar
        Tabs tabs = createTabs();

        // Content Area for Tabs
        Div tabContent = new Div();
        tabContent.setWidthFull();
        tabContent.getStyle().set("padding", "20px");

        tabs.addSelectedChangeListener(event -> {
            tabContent.removeAll();
            switch (tabs.getSelectedTab().getLabel()) {
                case "Homepage":
                    tabContent.add(createHomepageLayout());
                    break;
                case "Rooms":
                    tabContent.add(createRoomLayout());
                    break;
                case "Roomlist":
                    tabContent.add(createRoomListLayout());
                    break;
                default:
                    tabContent.add(new H3("Unknown Page"));
            }
        });

        // Add components to layout
        add(header, separator, tabs, tabContent);

        // Default content for Homepage
        tabContent.add(createHomepageLayout());
    }

    private Div createSeparator() {
        Div separator = new Div();
        separator.getStyle()
                .set("height", "5px")
                .set("background-color", "black")
                .set("margin", "10px 0")
                .set("width", "100%")
                .set("display", "block");
        return separator;
    }

    private Tabs createTabs() {
        Tab homeTab = new Tab("Homepage");
        Tab roomTab = new Tab("Rooms");
        Tab roomListTab = new Tab("Roomlist");

        Tabs tabs = new Tabs(homeTab, roomTab, roomListTab);
        tabs.getStyle()
                .set("background-color", "#FFFFFF")
                .set("margin-bottom", "10px");

        return tabs;
    }

    private VerticalLayout createHomepageLayout() {
        VerticalLayout homepageLayout = new VerticalLayout();

        HorizontalLayout totalRoomsLayout = createStatRow("Total Rooms: ", totalRoomsText = new Span("0"));
        HorizontalLayout roomsCleanedLayout = createStatRow("Rooms Cleaned: ", roomsCleanedText = new Span("0"));
        HorizontalLayout roomsNotCleanedLayout = createStatRow("Rooms Not Cleaned: ", roomsNotCleanedText = new Span("0"));

        homepageLayout.add(totalRoomsLayout, roomsCleanedLayout, roomsNotCleanedLayout);
        updateRoomStats();

        return homepageLayout;
    }

    private HorizontalLayout createStatRow(String label, Span value) {
        // Text for the stat
        H3 statLabel = new H3(label);
        statLabel.getStyle().set("margin-right", "10px");

        // Style for the value span
        value.getStyle().set("font-weight", "bold");

        // Create the horizontal layout
        HorizontalLayout statRow = new HorizontalLayout(statLabel, value);
        statRow.setAlignItems(Alignment.CENTER);
        statRow.setWidthFull();
        statRow.setSpacing(true);

        return statRow;
    }


    private HorizontalLayout createRoomLayout() {
        roomLayout = new HorizontalLayout();
        roomLayout.setWidthFull();
        roomLayout.setSpacing(true);
        refreshRoomLayout();
        return roomLayout;
    }

    private void refreshRoomLayout() {
        roomLayout.removeAll();
        List<Room> updatedRooms = manager_functions.getAllRooms();
        for (Room room : updatedRooms) {
            roomLayout.add(createRoomBox(room));
        }
    }

    private Div createRoomBox(Room room) {
        Div box = new Div();
        box.getStyle()
                .set("border", "1px solid #e0e0e0")
                .set("border-radius", "10px")
                .set("background-color", "#f9f9f9")
                .set("padding", "20px")
                .set("width", "300px")
                .set("text-align", "center");

        // Room Name
        H2 roomName = new H2(room.getRoomName());
        roomName.getStyle().set("font-size", "20px").set("margin-bottom", "10px");

        // Status Display
        Span statusDisplay = new Span(room.getStatus());
        statusDisplay.getStyle()
                .set("background-color", room.getStatus().equals("CLEANED") ? "#4caf50" : "#f44336")
                .set("color", "#ffffff")
                .set("padding", "5px 10px")
                .set("border-radius", "5px")
                .set("margin-bottom", "10px")
                .set("display", "inline-block");

        // Tasks Section
        H3 tasksHeader = new H3("Tasks");
        tasksHeader.getStyle().set("margin-bottom", "10px");

        VerticalLayout tasksLayout = new VerticalLayout();
        tasksLayout.setSpacing(false);
        tasksLayout.setPadding(false);

        for (Task task : room.getTasks()) {
            HorizontalLayout taskLayout = new HorizontalLayout();
            taskLayout.setWidthFull();

            // Task Name - Left Aligned
            Span taskName = new Span(task.getTaskName());
            taskName.getStyle().set("flex", "1");
            taskLayout.add(taskName);

            // Task Icon - Right Aligned
            Image taskStatusIcon = new Image(
                    task.getStatus().equals("CLEANED") ? "cleaned.png" : "not_cleaned.png",
                    task.getStatus()
            );
            taskStatusIcon.setHeight("20px");
            taskStatusIcon.getStyle().set("margin-left", "auto");
            taskStatusIcon.addClickListener(event -> {
                // Update the task status
                String newStatus = task.getStatus().equals("CLEANED") ? "NOT_CLEANED" : "CLEANED";
                manager_functions.updateTaskStatus(task.getId(), newStatus);

                // Update the room status dynamically
                manager_functions.updateRoomStatusBasedOnTasks(room.getId());

                // Refresh the layout dynamically without full reload
                refreshRoomLayout();
                updateRoomStats();
            });

            taskLayout.add(taskStatusIcon);
            tasksLayout.add(taskLayout);
        }

        box.add(roomName, statusDisplay, tasksHeader, tasksLayout);
        return box;
    }

    private void updateRoomStats() {
        List<Room> rooms = manager_functions.getAllRooms();
        long cleaned = rooms.stream().filter(room -> "CLEANED".equals(room.getStatus())).count();
        totalRoomsText.setText(String.valueOf(rooms.size()));
        roomsCleanedText.setText(String.valueOf(cleaned));
        roomsNotCleanedText.setText(String.valueOf(rooms.size() - cleaned));
    }

    private VerticalLayout createRoomListLayout() {
        VerticalLayout roomListLayout = new VerticalLayout();

        // Fetch data from the database
        List<Room> rooms = manager_functions.getAllRooms();

        // Create a grid
        Grid<Room> grid = new Grid<>(Room.class, false);
        grid.addColumn(Room::getRoomName).setHeader("Room Name");
        grid.addColumn(Room::getStatus).setHeader("Status");

        // Set initial data to grid
        grid.setItems(rooms);

        // Create buttons
        Button sortButton = new Button("SORT");
        sortButton.getStyle()
                .set("background-color", "#4caf50") // Green background
                .set("color", "white") // White text
                .set("font-weight", "bold") // Bold text
                .set("border", "none") // No border
                .set("padding", "10px 20px") // Padding for size
                .set("border-radius", "5px") // Rounded corners
                .set("cursor", "pointer"); // Pointer cursor

        Button backButton = new Button("BACK");
        backButton.getStyle()
                .set("background-color", "#f44336") // Red background
                .set("color", "white") // White text
                .set("font-weight", "bold") // Bold text
                .set("border", "none") // No border
                .set("padding", "10px 20px") // Padding for size
                .set("border-radius", "5px") // Rounded corners
                .set("cursor", "pointer"); // Pointer cursor

        // Add button click listeners
        sortButton.addClickListener(event -> {
            List<Room> notCleanedRooms = NotCleanedRooms(rooms); // Filter "NOT_CLEANED" rooms
            grid.setItems(notCleanedRooms); // Update grid to show only "NOT_CLEANED" rooms
        });

        backButton.addClickListener(event -> {
            grid.setItems(rooms); // Reset grid to show all rooms
        });

        // Create a vertical layout for buttons
        VerticalLayout buttonLayout = new VerticalLayout(sortButton, backButton);
        buttonLayout.setSpacing(true); // Space between buttons
        buttonLayout.setWidth("30%"); // Adjust button layout width
        buttonLayout.setAlignItems(Alignment.CENTER); // Center align buttons
        buttonLayout.getStyle().set("margin-left", "auto"); // Push buttons to the right

        // Create a horizontal layout for grid and buttons
        HorizontalLayout mainLayout = new HorizontalLayout(grid, buttonLayout);
        mainLayout.setWidth("100%"); // Ensure full-width layout
        mainLayout.setSpacing(false); // Remove horizontal spacing

        // Add components to the layout
        roomListLayout.add(new H2("Room List"), mainLayout);
        roomListLayout.setSpacing(false); // Reduce vertical spacing
        roomListLayout.setPadding(false); // Remove padding

        return roomListLayout;
    }


    private List<Room> NotCleanedRooms(List<Room> rooms) {
        List<Room> notCleanedRooms = new ArrayList<>();
        for (Room room : rooms) {
            if ("NOT_CLEANED".equals(room.getStatus())) {
                notCleanedRooms.add(room); // Add rooms with "NOT_CLEANED" status
            }
        }
        return notCleanedRooms; // Return the filtered list
    }



}
