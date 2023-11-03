# Cruise Line Management System (CLMS)

The Cruise Line Management System (CLMS) is a Java application for managing cruise ship data and passengers. It uses SQLite as the underlying database to store ship and passenger information.

## Features

- **Database Management**: The application handles database creation and table creation for ships and passengers.

- **Adding Ships**: You can add cruise ships to the database, providing details such as ship name, company, location, trip length, number of cabins, year of build, maintenance year, and more.

- **Adding Passengers**: You can add passengers to the database, including their ship name, first name, last name, email, and cabin type.

- **Loading Sample Data**: The application includes methods to preload sample ship and passenger data into the database.

- **Updating Ship Maintenance Year**: You can update the maintenance year of a ship.

- **Retrieving Ship and Passenger Data**: The application allows you to retrieve ship and passenger data from the database.

- **In-Service Status**: Ships can be marked as in-service or out of service based on their maintenance year.

## Database Structure

The application manages two database tables:

1. **Ships**: Contains information about cruise ships, including ship name, company, location, trip length, number of cabins, year of build, maintenance year, capacity, and destinations.

2. **Passengers**: Stores information about passengers, including their ship name, first name, last name, email, and cabin type.

## Usage

1. **Database Connection**: The application connects to the SQLite database specified by the `CLMSv2.db` file.

2. **Database Initialization**: The `createNewDatabase` method initializes the database by creating tables for ships and passengers. Sample data is loaded into the tables using the `loadShip` and `loadPassenger` methods.

3. **Add Ships and Passengers**: You can add ships and passengers to the database using the `insertShip` and `insertPassenger` methods.

4. **Update Ship Maintenance Year**: The `updateShip` method allows you to update the maintenance year of a ship.

5. **Retrieve Data**: You can retrieve ship and passenger data from the database using the `getAllShips` and `getAllPassengersForTable` methods.

6. **In-Service Status**: The in-service status of a ship is determined based on its maintenance year.
