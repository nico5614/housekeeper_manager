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
import java.util.LinkedList;
import java.util.ArrayList;

import org.example.housekeeping_manager.rooms.Room;
import org.example.housekeeping_manager.tasks.Task;

import java.util.List;

@Route("")
public class manager_gui extends VerticalLayout {

    private Span totalRoomsText; // Display for total rooms count
    private Span roomsCleanedText; // Display for cleaned rooms count
    private Span roomsNotCleanedText; // Display for not cleaned rooms count
    private HorizontalLayout roomLayout; // Layout for displaying individual room details

    public manager_gui() {
        setWidthFull(); // Set the width of the layout to occupy full screen
        setSpacing(true); // Add spacing between components
        setPadding(true); // Add padding around the layout

        // Header Section: Title, subtitle, and images
        H2 title = new H2("Housekeeper Manager"); // Main title
        Span subtitle = new Span("powered by Nico Köchli - Programming project 2, FHNW"); // Subtitle
        Image logo = new Image("fhnw_logo.jpg", "FHNW Logo"); // FHNW Logo
        Image maidImage = new Image("maid.png", "Maid Icon"); // Maid icon for decoration

        logo.setHeight("50px");
        maidImage.setHeight("80px");

        // Combine header components into a horizontal layout
        HorizontalLayout header = new HorizontalLayout(maidImage, new VerticalLayout(title, subtitle), logo);
        header.setAlignItems(Alignment.CENTER); // Align items in the center
        header.setWidthFull(); // Set full width for header
        header.getStyle()
                .set("background-color", "#ffffff") // Set background color
                .set("padding", "20px"); // Add padding to header

        // Black Separator Line
        Div separator = createSeparator(); // Creates a dividing line between sections

        // Tabs Navigation Bar
        Tabs tabs = createTabs(); // Creates tabs for navigation

        // Content Area for Tabs
        Div tabContent = new Div();
        tabContent.setWidthFull(); // Set full width for content area
        tabContent.getStyle().set("padding", "20px"); // Add padding

        // Add event listener for tab changes
        tabs.addSelectedChangeListener(event -> {
            tabContent.removeAll(); // Clear the content area
            switch (tabs.getSelectedTab().getLabel()) {
                case "Homepage":
                    tabContent.add(createHomepageLayout()); // Add homepage layout
                    break;
                case "Rooms":
                    tabContent.add(createRoomLayout()); // Add room layout
                    break;
                case "Roomlist":
                    tabContent.add(createRoomListLayout()); // Add room list layout
                    break;
                default:
                    tabContent.add(new H3("Unknown Page")); // Handle invalid tabs
            }
        });

        // Add header, separator, tabs, and content area to main layout
        add(header, separator, tabs, tabContent);

        // Default content for Homepage
        tabContent.add(createHomepageLayout());
    }

    private Div createSeparator() {
        /**
         * Creates a black horizontal line for visual separation.
         * Styling includes height, width, margin, and background color.
         */
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
        /**
         * Creates navigation tabs for switching between different views (Homepage, Rooms, Roomlist).
         * Uses Tab and Tabs components from Vaadin.
         */
        Tab homeTab = new Tab("Homepage");
        Tab roomTab = new Tab("Rooms");
        Tab roomListTab = new Tab("Roomlist");

        Tabs tabs = new Tabs(homeTab, roomTab, roomListTab);
        tabs.getStyle()
                .set("background-color", "#FFFFFF") // White background for tabs
                .set("margin-bottom", "10px"); // Add spacing below tabs

        return tabs;
    }

    private VerticalLayout createHomepageLayout() {
        /**
         * Layout for the homepage tab.
         * Includes statistics (total rooms, cleaned rooms, not cleaned rooms) and task history.
         */
        VerticalLayout homepageLayout = new VerticalLayout();

        // Statistics Section
        HorizontalLayout totalRoomsLayout = createStatRow("Total Rooms: ", totalRoomsText = new Span("0"));
        HorizontalLayout roomsCleanedLayout = createStatRow("Rooms Cleaned: ", roomsCleanedText = new Span("0"));
        HorizontalLayout roomsNotCleanedLayout = createStatRow("Rooms Not Cleaned: ", roomsNotCleanedText = new Span("0"));

        homepageLayout.add(totalRoomsLayout, roomsCleanedLayout, roomsNotCleanedLayout);

        // Update the statistics dynamically
        updateRoomStats();

        // Task History Section
        H3 historyTitle = new H3("Latest Task Changes"); // Title for task history section
        historyTitle.getStyle().set("margin-top", "20px").set("margin-bottom", "10px");

        // Create a container to display the history
        VerticalLayout taskHistoryLayout = new VerticalLayout();
        taskHistoryLayout.getStyle().set("background-color", "#f9f9f9")
                .set("border", "1px solid #e0e0e0")
                .set("padding", "10px")
                .set("border-radius", "5px");

        // Populate the task history dynamically
        updateTaskHistory(taskHistoryLayout);

        // Add the task history section to the homepage layout
        homepageLayout.add(historyTitle, taskHistoryLayout);

        return homepageLayout;
    }

    private HorizontalLayout createStatRow(String label, Span value) {
        /**
         * Creates a row for displaying a single statistic.
         * Example: "Total Rooms: 5"
         */
        H3 statLabel = new H3(label); // Label for the statistic
        statLabel.getStyle().set("margin-right", "10px"); // Add spacing to the right

        value.getStyle().set("font-weight", "bold"); // Bold font for the statistic value

        HorizontalLayout statRow = new HorizontalLayout(statLabel, value);
        statRow.setAlignItems(Alignment.CENTER); // Center align the label and value
        statRow.setWidthFull(); // Set full width
        statRow.setSpacing(true); // Add spacing between components

        return statRow;
    }

    private HorizontalLayout createRoomLayout() {
        /**
         * Layout for the "Rooms" tab.
         * Displays room details using a horizontal layout.
         */
        roomLayout = new HorizontalLayout();
        roomLayout.setWidthFull(); // Full width layout
        roomLayout.setSpacing(true); // Add spacing between components
        refreshRoomLayout(); // Populate with room data
        return roomLayout;
    }

    private void refreshRoomLayout() {
        /**
         * Refreshes the room layout with the latest data from the database.
         * Uses manager_functions.getAllRooms() to fetch updated room details.
         */
        roomLayout.removeAll(); // Clear existing components
        List<Room> updatedRooms = manager_functions.getAllRooms(); // Fetch updated rooms from database
        for (Room room : updatedRooms) {
            roomLayout.add(createRoomBox(room)); // Add each room as a box
        }
    }

    private Div createRoomBox(Room room) {
        /**
         * Creates a visual box to display room details.
         * Includes room name, status, and associated tasks.
         */
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
            /**
             * Lambda Expression:
             * room.getTasks() -> Iterates through each task in the room's task list.
             * task -> Represents the current task in the loop.
             */
            HorizontalLayout taskLayout = new HorizontalLayout();
            taskLayout.setWidthFull();

            Span taskName = new Span(task.getTaskName());
            taskName.getStyle().set("flex", "1");
            taskLayout.add(taskName);

            Image taskStatusIcon = new Image(
                    task.getStatus().equals("CLEANED") ? "cleaned.png" : "not_cleaned.png",
                    task.getStatus()
            );
            taskStatusIcon.setHeight("20px");
            taskStatusIcon.getStyle().set("margin-left", "auto");
            taskStatusIcon.addClickListener(event -> {
                /**
                 * Updates the task status and refreshes the layout dynamically.
                 * Event listener for the task status icon.
                 */
                String newStatus = task.getStatus().equals("CLEANED") ? "NOT_CLEANED" : "CLEANED";
                manager_functions.updateTaskStatus(task.getId(), newStatus);
                manager_functions.updateRoomStatusBasedOnTasks(room.getId());
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
        /**
         * Updates the room statistics on the homepage.
         * Fetches data using manager_functions.getAllRooms() and calculates statistics dynamically.
         */
        List<Room> rooms = manager_functions.getAllRooms();
        long cleaned = rooms.stream().filter(room -> "CLEANED".equals(room.getStatus())).count();
        totalRoomsText.setText(String.valueOf(rooms.size()));
        roomsCleanedText.setText(String.valueOf(cleaned));
        roomsNotCleanedText.setText(String.valueOf(rooms.size() - cleaned));
    }

    private VerticalLayout createRoomListLayout() {
        /**
         * Layout for the "Roomlist" tab.
         * Displays all rooms in a grid with options to filter and reset.
         */
        VerticalLayout roomListLayout = new VerticalLayout();

        List<Room> rooms = manager_functions.getAllRooms(); // Fetch rooms from database

        Grid<Room> grid = new Grid<>(Room.class, false); // Create a grid to display rooms
        grid.addColumn(Room::getRoomName).setHeader("Room Name");
        grid.addColumn(Room::getStatus).setHeader("Status");

        grid.setItems(rooms); // Set data for the grid

        Button sortButton = new Button("SORT");
        sortButton.addClickListener(event -> {
            /**
             * Filters rooms to display only those with status "NOT_CLEANED".
             * Updates the grid dynamically.
             */
            List<Room> notCleanedRooms = NotCleanedRooms(rooms);
            grid.setItems(notCleanedRooms);
        });

        Button backButton = new Button("BACK");
        backButton.addClickListener(event -> {
            /**
             * Resets the grid to display all rooms.
             */
            grid.setItems(rooms);
        });

        VerticalLayout buttonLayout = new VerticalLayout(sortButton, backButton);
        buttonLayout.setSpacing(true);
        buttonLayout.setWidth("30%");
        buttonLayout.setAlignItems(Alignment.CENTER);

        HorizontalLayout mainLayout = new HorizontalLayout(grid, buttonLayout);
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(false);

        roomListLayout.add(new H2("Room List"), mainLayout);
        return roomListLayout;
    }

    private List<Room> NotCleanedRooms(List<Room> rooms) {
        /**
         * Filters rooms that are "NOT_CLEANED" using a simple for-loop.
         * Adds filtered rooms to a new list and returns it.
         */
        List<Room> notCleanedRooms = new ArrayList<>();
        for (Room room : rooms) {
            if ("NOT_CLEANED".equals(room.getStatus())) {
                notCleanedRooms.add(room);
            }
        }
        return notCleanedRooms;
    }

    private void updateTaskHistory(VerticalLayout taskHistoryLayout) {
        /**
         * Updates the task history display on the homepage.
         * Fetches recent task history using manager_functions.getTaskHistory().
         */
        taskHistoryLayout.removeAll(); // Clear existing history
        LinkedList<String> history = manager_functions.getTaskHistory(); // Fetch task history

        for (String taskChange : history) {
            /**
             * Iterates through each task history entry and adds it to the layout.
             */
            Span historyEntry = new Span(taskChange);
            historyEntry.getStyle().set("font-size", "14px").set("color", "#333");
            taskHistoryLayout.add(historyEntry);
        }
    }
}
