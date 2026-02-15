package ui;

import enums.Location;
import enums.PackageStatus;
import exception.InvalidInputException;
import model.*;
import persistence.StorageService;
import service.*;
import util.DateTimeFormatterUtil;
import util.InputValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner;
    private final PackageService packageService;
    private final RouteService routeService;
    private final TruckService truckService;
    private final StorageService storageService;
    private final repository.InMemoryDataStore dataStore;

    public ConsoleMenu(PackageService packageService, RouteService routeService,
                       TruckService truckService, StorageService storageService,
                       repository.InMemoryDataStore dataStore) {
        this.scanner = new Scanner(System.in);
        this.packageService = packageService;
        this.routeService = routeService;
        this.truckService = truckService;
        this.storageService = storageService;
        this.dataStore = dataStore;
    }

    public void run() {
        System.out.println("********************************************");
        System.out.println("*    Australian Logistics Management App   *");
        System.out.println("********************************************");
        System.out.println();

        loadOnStartup();
        mainLoop();

        scanner.close();
        System.out.println("Goodbye!");
    }

    private void loadOnStartup() {
        if (storageService.hasSavedData()) {
            System.out.print("Saved data found. Load it? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                try {
                    InMemoryData loaded = storageService.load();
                    if (loaded != null) {
                        dataStore.setData(loaded);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading data: " + e.getMessage());
                    System.out.println("Starting with fresh data.");
                }
            }
        }
        truckService.initializeFleet();
    }

    private void mainLoop() {
        while (true) {
            printMenu();
            String input = scanner.nextLine().trim();
            System.out.println();

            try {
                switch (input) {
                    case "1":  createPackage();         break;
                    case "2":  createRoute();           break;
                    case "3":  searchRoutes();          break;
                    case "4":  assignTruckToRoute();    break;
                    case "5":  assignPackageToRoute();  break;
                    case "6":  bulkAssignPackages();    break;
                    case "7":  viewAllRoutes();         break;
                    case "8":  viewRoutesInProgress();  break;
                    case "9":  viewAllPackages();       break;
                    case "10": viewUnassignedPackages();break;
                    case "11": viewPackageById();       break;
                    case "12": viewAllTrucks();         break;
                    case "13": viewFreeTrucks();        break;
                    case "14": saveData();              break;
                    case "15": loadData();              break;
                    case "0":
                        System.out.print("Save before exiting? (y/n): ");
                        String saveChoice = scanner.nextLine().trim().toLowerCase();
                        if (saveChoice.equals("y") || saveChoice.equals("yes")) {
                            saveData();
                        }
                        return;
                    default:
                        System.out.println("Invalid option. Please enter a number from the menu.");
                }
            } catch (InvalidInputException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("*************** MAIN MENU ***************");
        System.out.println("  1.  Create a delivery package");
        System.out.println("  2.  Create a delivery route");
        System.out.println("  3.  Search for a route");
        System.out.println("  4.  Assign truck to a route");
        System.out.println("  5.  Assign package to a route");
        System.out.println("  6.  Bulk assign packages to a route");
        System.out.println("  7.  View all routes");
        System.out.println("  8.  View routes in progress");
        System.out.println("  9.  View all packages");
        System.out.println("  10. View unassigned packages");
        System.out.println("  11. View package by ID");
        System.out.println("  12. View all trucks");
        System.out.println("  13. View free trucks");
        System.out.println("  14. Save data");
        System.out.println("  15. Load data");
        System.out.println("  0.  Exit");
        System.out.println("*****************************************");
        System.out.print("Choose an option: ");
    }

    // ======================== 1. CREATE PACKAGE ========================

    private void createPackage() {
        System.out.println("--- Create Delivery Package ---");
        printAvailableCities();

        System.out.print("Start location: ");
        Location start = InputValidator.requireValidLocation(scanner.nextLine());

        System.out.print("End location: ");
        Location end = InputValidator.requireValidLocation(scanner.nextLine());

        if (start == end) {
            System.out.println("Error: Start and end locations must be different.");
            return;
        }

        System.out.print("Weight (kg): ");
        double weight = InputValidator.requirePositiveDouble(scanner.nextLine(), "Weight");

        System.out.print("Customer first name: ");
        String firstName = InputValidator.requireNonEmpty(scanner.nextLine(), "First name");

        System.out.print("Customer last name: ");
        String lastName = InputValidator.requireNonEmpty(scanner.nextLine(), "Last name");

        System.out.print("Customer address: ");
        String address = InputValidator.requireNonEmpty(scanner.nextLine(), "Address");

        ContactInfo contact = new ContactInfo(firstName, lastName, address);
        DeliveryPackage pkg = packageService.createPackage(start, end, weight, contact);

        System.out.println("\nPackage created successfully!");
        System.out.println(pkg);
    }

    // ======================== 2. CREATE ROUTE ========================

    private void createRoute() {
        System.out.println("--- Create Delivery Route ---");
        printAvailableCities();

        System.out.print("Enter locations separated by commas (e.g. ASP,ADL,MEL,SYD,BRI): ");
        String locInput = scanner.nextLine().trim();
        String[] parts = locInput.split(",");

        List<Location> locations = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                locations.add(Location.fromString(trimmed));
            }
        }

        if (locations.size() < 2) {
            System.out.println("Error: At least two locations are required.");
            return;
        }

        System.out.print("Departure date/time (yyyy-MM-dd HH:mm): ");
        LocalDateTime departure = DateTimeFormatterUtil.parse(scanner.nextLine());

        DeliveryRoute route = routeService.createRoute(locations, departure);
        System.out.println("\nRoute created successfully!");
        System.out.println(route.toDetailedString());
    }

    // ======================== 3. SEARCH ROUTES ========================

    private void searchRoutes() {
        System.out.println("--- Search for Route ---");
        printAvailableCities();

        System.out.print("Package start location: ");
        Location start = InputValidator.requireValidLocation(scanner.nextLine());

        System.out.print("Package end location: ");
        Location end = InputValidator.requireValidLocation(scanner.nextLine());

        List<DeliveryRoute> found = routeService.searchRoutes(start, end);
        if (found.isEmpty()) {
            System.out.println("No routes found serving " + start + " → " + end);
        } else {
            System.out.println("\nFound " + found.size() + " route(s):\n");
            found.forEach(r -> {
                System.out.println(r.toDetailedString());
                if (r.getAssignedTruck() != null) {
                    System.out.printf("  Free capacity: %.1f kg%n", r.getFreeCapacity());
                }
                System.out.println();
            });
        }
    }

    // ======================== 4. ASSIGN TRUCK ========================

    private void assignTruckToRoute() {
        System.out.println("--- Assign Truck to Route ---");

        System.out.print("Route ID: ");
        int routeId = InputValidator.parseInt(scanner.nextLine(), "Route ID");

        DeliveryRoute route = routeService.findById(routeId)
                .orElseThrow(() -> new InvalidInputException("Route #" + routeId + " not found."));
        System.out.println(route.toDetailedString());

        List<Truck> suitable = truckService.findSuitableForRoute(0, route.getTotalDistanceKm());
        if (suitable.isEmpty()) {
            System.out.println("No free trucks available with sufficient range for this route.");
            return;
        }
        System.out.println("Available trucks:");
        suitable.forEach(t -> System.out.println("  " + t));

        System.out.print("\nTruck ID to assign: ");
        int truckId = InputValidator.parseInt(scanner.nextLine(), "Truck ID");

        Truck truck = truckService.findById(truckId)
                .orElseThrow(() -> new InvalidInputException("Truck #" + truckId + " not found."));

        routeService.assignTruck(route, truck);
        System.out.println("Truck #" + truckId + " assigned to Route #" + routeId + " successfully!");
    }

    // ======================== 5. ASSIGN PACKAGE ========================

    private void assignPackageToRoute() {
        System.out.println("--- Assign Package to Route ---");

        System.out.print("Package ID: ");
        int packageId = InputValidator.parseInt(scanner.nextLine(), "Package ID");

        System.out.print("Route ID: ");
        int routeId = InputValidator.parseInt(scanner.nextLine(), "Route ID");

        DeliveryPackage pkg = packageService.findById(packageId)
                .orElseThrow(() -> new InvalidInputException("Package #" + packageId + " not found."));
        DeliveryRoute route = routeService.findById(routeId)
                .orElseThrow(() -> new InvalidInputException("Route #" + routeId + " not found."));

        routeService.assignPackage(route, pkg);
        System.out.println("Package #" + packageId + " assigned to Route #" + routeId + " successfully!");
        if (pkg.getExpectedArrival() != null) {
            System.out.println("Expected arrival: " + DateTimeFormatterUtil.format(pkg.getExpectedArrival()));
        }
    }

    // ======================== 6. BULK ASSIGN ========================

    private void bulkAssignPackages() {
        System.out.println("--- Bulk Assign Packages to Route ---");

        System.out.print("Route ID: ");
        int routeId = InputValidator.parseInt(scanner.nextLine(), "Route ID");

        DeliveryRoute route = routeService.findById(routeId)
                .orElseThrow(() -> new InvalidInputException("Route #" + routeId + " not found."));
        System.out.println(route.toDetailedString());

        System.out.print("Package IDs (comma-separated, e.g. 1,2,3): ");
        String input = scanner.nextLine().trim();
        String[] parts = input.split(",");

        int successCount = 0;
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) continue;
            try {
                int packageId = Integer.parseInt(trimmed);
                DeliveryPackage pkg = packageService.findById(packageId)
                        .orElseThrow(() -> new InvalidInputException("Package #" + packageId + " not found."));
                routeService.assignPackage(route, pkg);
                System.out.println("  Package #" + packageId + " assigned successfully.");
                successCount++;
            } catch (Exception e) {
                System.out.println("  Package " + trimmed + " failed: " + e.getMessage());
            }
        }
        System.out.println(successCount + " package(s) assigned to Route #" + routeId);
    }

    // ======================== 7. VIEW ALL ROUTES ========================

    private void viewAllRoutes() {
        List<DeliveryRoute> routes = routeService.findAll();
        if (routes.isEmpty()) {
            System.out.println("No routes have been created yet.");
            return;
        }
        System.out.println("--- All Delivery Routes (" + routes.size() + ") ---\n");
        routes.forEach(r -> {
            System.out.println(r.toDetailedString());
            System.out.println();
        });
    }

    // ======================== 8. VIEW ROUTES IN PROGRESS ========================

    private void viewRoutesInProgress() {
        List<DeliveryRoute> routes = routeService.findInProgress();
        LocalDateTime now = LocalDateTime.now();

        if (routes.isEmpty()) {
            System.out.println("No routes currently in progress.");
            return;
        }
        System.out.println("--- Routes In Progress (" + routes.size() + ") ---\n");
        routes.forEach(r -> {
            System.out.println(r.toDetailedString());
            System.out.println("  Current status: " + r.getCurrentStopInfo(now));
            System.out.println();
        });
    }

    // ======================== 9. VIEW ALL PACKAGES ========================

    private void viewAllPackages() {
        List<DeliveryPackage> packages = packageService.findAll();
        if (packages.isEmpty()) {
            System.out.println("No packages have been created yet.");
            return;
        }
        System.out.println("--- All Packages (" + packages.size() + ") ---\n");
        packages.forEach(System.out::println);
    }

    // ======================== 10. VIEW UNASSIGNED PACKAGES ========================

    private void viewUnassignedPackages() {
        List<DeliveryPackage> unassigned = packageService.findUnassigned();
        if (unassigned.isEmpty()) {
            System.out.println("All packages are assigned to routes.");
            return;
        }
        System.out.println("--- Unassigned Packages (" + unassigned.size() + ") ---\n");
        unassigned.forEach(p ->
                System.out.printf("  Package #%d | %s → %s | %.1f kg%n",
                        p.getId(), p.getStartLocation(), p.getEndLocation(), p.getWeight()));
    }

    // ======================== 11. VIEW PACKAGE BY ID ========================

    private void viewPackageById() {
        System.out.print("Enter package ID: ");
        int id = InputValidator.parseInt(scanner.nextLine(), "Package ID");

        Optional<DeliveryPackage> pkgOpt = packageService.findById(id);
        if (pkgOpt.isPresent()) {
            DeliveryPackage pkg = pkgOpt.get();
            System.out.println();
            System.out.println(pkg);

            if (pkg.getStatus() == PackageStatus.ASSIGNED || pkg.getStatus() == PackageStatus.IN_TRANSIT) {
                // Find the route this package is on
                routeService.findAll().stream()
                        .filter(r -> r.getPackages().stream().anyMatch(p -> p.getId() == pkg.getId()))
                        .findFirst()
                        .ifPresent(r -> {
                            System.out.println("Route details:");
                            System.out.println(r.toDetailedString());
                            System.out.println("  Current status: " + r.getCurrentStopInfo(LocalDateTime.now()));
                        });
            }
        } else {
            System.out.println("Package #" + id + " not found.");
        }
    }

    // ======================== 12. VIEW ALL TRUCKS ========================

    private void viewAllTrucks() {
        List<Truck> trucks = truckService.findAll();
        System.out.println("--- All Trucks (" + trucks.size() + ") ---\n");
        trucks.forEach(t -> System.out.println("  " + t));
    }

    // ======================== 13. VIEW FREE TRUCKS ========================

    private void viewFreeTrucks() {
        List<Truck> free = truckService.findFree();
        if (free.isEmpty()) {
            System.out.println("No free trucks available.");
            return;
        }
        System.out.println("--- Free Trucks (" + free.size() + ") ---\n");
        free.forEach(t -> System.out.println("  " + t));
    }

    // ======================== 14. SAVE ========================

    private void saveData() {
        try {
            storageService.save(dataStore.getData());
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // ======================== 15. LOAD ========================

    private void loadData() {
        try {
            InMemoryData loaded = storageService.load();
            if (loaded != null) {
                dataStore.setData(loaded);
                System.out.println("Data loaded successfully.");
            } else {
                System.out.println("No saved data found.");
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // ======================== HELPERS ========================

    private void printAvailableCities() {
        System.out.println("Available cities:");
        for (Location loc : Location.values()) {
            System.out.printf("  %s - %s%n", loc.name(), loc.getFullName());
        }
    }
}
