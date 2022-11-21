//  Console log message error appears when creates database fail 
function transactionError(tx, error) {
    log(`SQL Error ${error.code}. Message: ${error.message}.`, ERROR);
}

// Console log message appears when creates table successfully
function transactionSuccessForTable(tableName) {
    log(`Create table '${tableName}' successfully.`);
}

// Console log message appears when insert id and name into table successfully
function transactionSuccessForTableData(tableName, id, name) {
    log(`Insert (${id}, "${name}") into '${tableName}' successfully.`);
}

function prepareDatabase(db) {
    db.transaction(function (tx) {
        // Declare a query variable to set the fields for the Trip table.
        let query = `CREATE TABLE IF NOT EXISTS Trip (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT UNIQUE NOT NULL,
            Destination TEXT NOT NULL,
            Date REAL NOT NULL,
            Risk TEXT NOT NULL,
            Description TEXT NULL,
            Transportation Text NOT NULL,
            Upgrade Text NULL
        )`;
        // Create trip table into database
        tx.executeSql(query, [], transactionSuccessForTable('Trip'), transactionError);

        // Declare a query variable to set the fields for the Expense table.
        query = `CREATE TABLE IF NOT EXISTS Expense (
            Id INTEGER PRIMARY KEY AUTOINCREMENT,
            Type TEXT NOT NULL,
            Amount INTEGER NOT NULL,
            Date REAL NOT NULL,
            Time REAL NOT NULL,
            Comment TEXT NULL,
            TripId INTEGER NOT NULL,
            FOREIGN KEY (TripId) REFERENCES Trip(Id) ON DELETE CASCADE
        )`;
        // Create expense table into database
        tx.executeSql(query, [], transactionSuccessForTable('Expense'), transactionError);
    });
}

