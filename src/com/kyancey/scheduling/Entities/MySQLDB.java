package com.kyancey.scheduling.Entities;

import com.kyancey.scheduling.Data.*;
import com.kyancey.scheduling.Data.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A singleton representing the DB connection.
 */
public class MySQLDB {
    private static MySQLDB _instance;

    static {
        try {
            _instance = new MySQLDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Connection connection;

    /**
     * Private constructor for MySQLDB singleton. Contains sensitive information.
     * TODO: Move this information into a secrets file. It's a security hole as is.
     *
     * @throws SQLException
     */
    private MySQLDB() throws SQLException {
        this.connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/WJ08nm9", "root", "password"); //replace with real values
    }

    /**
     * @return DB instance object for scheduling DB.
     */
    public static MySQLDB getInstance() {
        return _instance;
    }

    /**
     * @return DB connection object for scheduling DB.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Searches for the country associated with the division
     * @param division Division for which the associated country is searched
     * @return
     * @throws SQLException
     */
    public static Country getCountry(Division division) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT COUNTRY_ID " +
                                                                            "FROM first_level_divisions " +
                                                                            "WHERE Division_ID = ?");
        statement.setInt(1, division.getId());
        var rs = statement.executeQuery();

        if (!rs.next()) {
            throw new SQLException("Non-existent country id");
        }

        var countryID = rs.getInt(1);

        for (var country : MySQLDB.CountryList()) {
            if (countryID == country.getId()) {
                return country;
            }
        }

        // Just here to keep the IDE from complaining
        return new Country();
    }

    /**
     * Returns the division object associated with the Division ID
     * @param divisionID Division ID
     * @return
     * @throws SQLException
     */
    public static Division getDivision(int divisionID) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT Division " +
                                                                            "FROM first_level_divisions " +
                                                                            "WHERE Division_ID = ?");
        statement.setInt(1, divisionID);
        var rs = statement.executeQuery();

        if (!rs.next()) {
            throw new SQLException("Non-existent division id");
        }

        var division = new Division();
        division.setId(divisionID);
        division.setName(rs.getString(1));

        return division;
    }

    /**
     * Returns the Contact object associated with the Contact ID
     * @param contactID Contact ID
     * @return
     * @throws SQLException
     */
    public static Contact getContact(int contactID) throws SQLException {
        Contact result = null;
        for (var contact: MySQLDB.ContactList()) {
            if (contact.getContactID() == contactID) {
                result = contact;
            }
        }
        return result;
    }

    /**
     * @return List of all customers in the database
     * @throws SQLException
     */
    public static List<Customer> CustomerList() throws SQLException {
        List<Customer> customerList = new ArrayList();

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT * from customers");
        var rs = statement.executeQuery();

        while (rs.next()) {
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            LocalDateTime lastUpdated = rs.getTimestamp("Last_Update").toLocalDateTime();
            var customer = new Customer.CustomerBuilder()
                    .customerId(rs.getInt("Customer_ID"))
                    .customerName(rs.getString("Customer_Name"))
                    .customerAddress(rs.getString("Address"))
                    .postalCode(rs.getString("Postal_Code"))
                    .phone(rs.getString("Phone"))
                    .created(createDate)
                    .createdBy(rs.getString("Created_By"))
                    .lastUpdated(lastUpdated)
                    .lastUpdatedBy(rs.getString("Last_Updated_By"))
                    .divisionID(rs.getInt("Division_ID"))
                    .build();
            customerList.add(customer);
        }
        return customerList;
    }

    /**
     * @return List of all appointments in database
     * @throws SQLException
     */
    public static List<Appointment> AppointmentList() throws SQLException {
        List<Appointment> appointmentList = new ArrayList();

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT * from appointments");
        var rs = statement.executeQuery();

        while (rs.next()) {
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            LocalDateTime lastUpdated = rs.getTimestamp("Last_Update").toLocalDateTime();
            LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
            var appointment = new Appointment.AppointmentBuilder()
                    .appointmentId(rs.getInt("Appointment_ID"))
                    .title(rs.getString("Title"))
                    .description(rs.getString("Description"))
                    .location(rs.getString("Location"))
                    .type(rs.getString("Type"))
                    .start(start)
                    .end(end)
                    .created(createDate)
                    .createdBy(rs.getString("Created_By"))
                    .lastUpdated(lastUpdated)
                    .lastUpdatedBy(rs.getString("Last_Updated_By"))
                    .customerID(rs.getInt("Customer_ID"))
                    .userID(rs.getInt("User_ID"))
                    .contactID(rs.getInt("Contact_ID"))
                    .build();
            appointmentList.add(appointment);
        }
        return appointmentList;
    }

    /**
     * @return List of all Countries in database.
     * @throws SQLException
     */
    public static List<Country> CountryList() throws SQLException {
        List<Country> countryList = new ArrayList<Country>();

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT Country_ID, Country " +
                                                                            "from countries " +
                                                                            "ORDER BY Country");
        var rs = statement.executeQuery();

        while (rs.next()) {
            var country = new Country();
            country.setId(rs.getInt("Country_ID"));
            country.setName(rs.getString("Country"));
            countryList.add(country);
        }
        return countryList;
    }

    /**
     * Returns a list of all Divisions for the provided Country
     * @param country Country object
     * @return
     * @throws SQLException
     */
    public static List<Division> DivisionList(Country country) throws SQLException {
        List<Division> divisionList = new ArrayList<Division>();

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT Division_ID, Division " +
                                                                            "from first_level_divisions " +
                                                                            "where COUNTRY_ID = ? " +
                                                                            "ORDER BY Division");
        statement.setString(1, Integer.toString(country.getId()));
        var rs = statement.executeQuery();

        while (rs.next()) {
            var division = new Division();
            division.setId(rs.getInt("Division_ID"));
            division.setName(rs.getString("Division"));
            division.setCountry_id(country.getId());
            divisionList.add(division);
        }
        return divisionList;
    }

    /**
     * @return List of all Contacts in database
     * @throws SQLException
     */
    public static List<Contact> ContactList() throws SQLException {
        List<Contact> contactList = new ArrayList<Contact>();

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT Contact_ID, Contact_Name, Email " +
                "from contacts " +
                "ORDER BY Contact_Name");
        var rs = statement.executeQuery();

        while (rs.next()) {
            var contact = new Contact();
            contact.setContactID(rs.getInt("Contact_ID"));
            contact.setName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
            contactList.add(contact);
        }
        return contactList;
    }

    /**
     * Inserts customer into database
     * @param customer Customer object
     * @return
     * @throws SQLException
     */
    public static boolean insertCustomer(Customer customer) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("INSERT into customers " +
                                                                            "(Customer_Name, Address, Postal_Code, " +
                                                                            "Phone, Create_Date, Created_By, Last_Update, " +
                                                                            "Last_Updated_By, Division_ID) " +
                                                                            "VALUES (?, ? , ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, customer.getCustomerName());
        statement.setString(2, customer.getCustomerAddress());
        statement.setString(3, customer.getPostalCode());
        statement.setString(4, customer.getPhone());

        // Creation Date and creator
        statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(6, CurrentUser.getCurrentUser());

        // Update Date and updater
        statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(8, CurrentUser.getCurrentUser());

        statement.setInt(9, customer.getDivisionID());
        var result = statement.executeUpdate();

        return (result == 1) ? true : false;
    }

    /**
     * Updates customer in database
     * @param customer Customer object
     * @throws SQLException
     */
    public static void updateCustomer(Customer customer) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("UPDATE customers " +
                                                                            "SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                                                                            "Phone = ?, Last_Update = ?, " +
                                                                            "Last_Updated_By = ?, Division_ID = ? " +
                                                                            "WHERE customer_ID = ?");
        statement.setString(1, customer.getCustomerName());
        statement.setString(2, customer.getCustomerAddress());
        statement.setString(3, customer.getPostalCode());
        statement.setString(4, customer.getPhone());

        // Update Date and updater
        statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        statement.setString(6, CurrentUser.getCurrentUser());

        statement.setInt(7, customer.getDivisionID());
        statement.setInt(8, customer.getCustomerId());
        var result = statement.executeUpdate();
    }

    /**
     * Updates appointment in database
     * @param appointment Appointment object
     * @throws SQLException
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("UPDATE appointments " +
                                                                            "SET Title = ?, Description = ?, " +
                                                                            "Location = ?, Type = ?, Start = ?, " +
                                                                            "End = ?, " +
                                                                            "Last_Update = ?, Last_Updated_By = ?, " +
                                                                            "Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                                                                            "WHERE Appointment_ID = ?");

        statement.setString(1, appointment.getTitle());
        statement.setString(2, appointment.getDescription());
        statement.setString(3, appointment.getLocation());
        statement.setString(4, appointment.getType());
        statement.setTimestamp(5, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(appointment.getStart()).toLocalDateTime()));
        statement.setTimestamp(6, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(appointment.getEnd()).toLocalDateTime()));
        statement.setTimestamp(7, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(LocalDateTime.now()).toLocalDateTime()));
        statement.setString(8, CurrentUser.getCurrentUser());
        statement.setInt(9, appointment.getCustomerID());
        statement.setInt(10, appointment.getUserID());
        statement.setInt(11, appointment.getContactID());
        statement.setInt(12, appointment.getAppointmentId());
        var result = statement.executeUpdate();
    }

    /**
     * Inserts appointment into database
     * @param appointment Appointment object
     * @return
     * @throws SQLException
     */
    public static boolean insertAppointment(Appointment appointment) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("INSERT into appointments " +
                "(Title, Description, Location, Type, " +
                "Start, End, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, appointment.getTitle());
        statement.setString(2, appointment.getDescription());
        statement.setString(3, appointment.getLocation());
        statement.setString(4, appointment.getType());
        statement.setTimestamp(5, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(appointment.getStart()).toLocalDateTime()));
        statement.setTimestamp(6, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(appointment.getEnd()).toLocalDateTime()));

        // Creation Date and creator
        statement.setTimestamp(7, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(LocalDateTime.now()).toLocalDateTime()));
        statement.setString(8, CurrentUser.getCurrentUser());

        // Update Date and updater
        statement.setTimestamp(9, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(LocalDateTime.now()).toLocalDateTime()));
        statement.setString(10, CurrentUser.getCurrentUser());

        statement.setInt(11, appointment.getCustomerID());
        statement.setInt(12, appointment.getUserID());
        statement.setInt(13, appointment.getContactID());

        var result = statement.executeUpdate();

        return (result == 1) ? true : false;
    }

    /**
     * Returns true if any of the customer's
     * appointments overlap with the provided time frame.
     * @param customerID Customer ID
     * @param localStartTime Start time of appointment
     * @param localEndTime End time of appointment
     * @return True if any of the customers appointments overlap
     * @throws SQLException
     */
    public static boolean customerAppointmentOverlaps(int customerID, LocalDateTime localStartTime, LocalDateTime localEndTime) throws SQLException {
        Timestamp sqlUTCStartTime = Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(localStartTime).toLocalDateTime());
        Timestamp sqlUTCEndTime = Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(localEndTime).toLocalDateTime());

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT * FROM appointments " +
                "WHERE (Customer_ID = ?) AND ((? BETWEEN Start and End) or (? BETWEEN Start and End))");
        statement.setInt(1, customerID);
        statement.setTimestamp(2, sqlUTCStartTime);
        statement.setTimestamp(3, sqlUTCEndTime);
        var rs = statement.executeQuery();

        if (!rs.next()) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Searches for an appointment for the user
     * within the next 15 minutes. Loads that
     * appointment's info into the appointment
     * object provided.
     * @param userName User name
     * @param start Current time
     * @param appointment Blank appointment object
     * @return True if appointment found
     * @throws SQLException
     */
    public static boolean userAppointmentOverlaps15Minute(String userName, LocalDateTime start, Appointment appointment) throws SQLException {

        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("SELECT User_ID from users WHERE " +
                                                                            "User_Name = ?");
        statement.setString(1, userName);
        var rs = statement.executeQuery();

        if (!rs.next()) {
            return false;
	    }
        int userID = rs.getInt(1);

        statement = sqlConnection.prepareStatement("SELECT * FROM appointments " +
                "WHERE User_ID = ? AND (Start BETWEEN ? AND DATE_ADD(?, INTERVAL 15 MINUTE))");
        statement.setInt(1, userID);
        statement.setTimestamp(2, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(start).toLocalDateTime()));
        statement.setTimestamp(3, Timestamp.valueOf(DateTimeConversion.toUTCFromLocal(start).toLocalDateTime()));
        rs = statement.executeQuery();

        if (!rs.next()) {
            return false;
        }
        else {
            appointment.setAppointmentId(rs.getInt(1));
            appointment.setTitle(rs.getString(2));
            appointment.setDescription(rs.getString(3));
            appointment.setLocation(rs.getString(4));
            appointment.setType(rs.getString(5));
            appointment.setStart(rs.getTimestamp(6).toLocalDateTime());
            appointment.setEnd(rs.getTimestamp(7).toLocalDateTime());
            appointment.setCreated(rs.getTimestamp(8).toLocalDateTime());
            appointment.setCreatedBy(rs.getString(9));
            appointment.setLastUpdated(rs.getTimestamp(10).toLocalDateTime());
            appointment.setLastUpdatedBy(rs.getString(11));
            appointment.setCustomerID(rs.getInt(12));
            appointment.setUserID(rs.getInt(13));
            appointment.setContactID(rs.getInt(14));
            return true;
        }
    }

    /**
     * Removes appointment from database.
     * @param appointment Appointment object
     * @throws SQLException
     */
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?");
        statement.setInt(1, appointment.getAppointmentId());
        statement.executeUpdate();
    }


    /**
     * Removes customer from database.
     * Removes all appointments associated
     * with customer first.
     * @param customer Customer object
     * @throws SQLException
     */
    public static void deleteCustomer(Customer customer) throws SQLException {
        var sqlConnection = MySQLDB.getInstance().getConnection();

        var statement = sqlConnection.prepareStatement("DELETE FROM customers WHERE Customer_ID = ?");
        statement.setInt(1, customer.getCustomerId());
        statement.executeUpdate();
    }
}
