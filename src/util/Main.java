package util;

import persistence.FileStorageService;
import persistence.StorageService;
import repository.InMemoryDataStore;
import repository.InMemoryDataStoreImpl;
import repository.PackageRepository;
import repository.PackageRepositoryImpl;
import repository.RouteRepository;
import repository.RouteServiceImpl;
import repository.TruckRepository;
import repository.TruckServiceImpl;
import service.DistanceService;
import service.PackageService;
import service.RouteService;
import service.TimeEstiomationService;
import service.TruckService;
import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {


        InMemoryDataStore dataStore = InMemoryDataStoreImpl.getInstance();


        PackageRepository packageRepo = new PackageRepositoryImpl(dataStore);
        TruckRepository truckRepo = new TruckServiceImpl(dataStore);
        RouteRepository routeRepo = new RouteServiceImpl(dataStore);


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