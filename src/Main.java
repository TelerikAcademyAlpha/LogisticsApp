import persistence.FileStorageService;
import persistence.StorageService;
import repository.*;
import service.*;
import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        InMemoryDataStore dataStore = InMemoryDataStoreImpl.getInstance();


        PackageRepository packageRepo = new PackageRepositoryImpl(dataStore);
        TruckRepository truckRepo = new TruckRepositoryImpl(dataStore);
        RouteRepository routeRepo = new RouteRepositoryImpl(dataStore);


        DistanceService distanceService = new DistanceService();
        TimeEstiomationService timeService = new TimeEstiomationService(distanceService);


        PackageService packageService = new PackageService(packageRepo);
        TruckService truckService = new TruckService(truckRepo);
        RouteService routeService = new RouteService(routeRepo, distanceService, timeService);


        StorageService storageService = new FileStorageService();


        ConsoleMenu menu = new ConsoleMenu(
                packageService,
                routeService,
                truckService,
                storageService,
                dataStore
        );


        menu.run();
    }
}
