let ERROR = 'ERROR';
let currentTripId = 'currentTripId';
// Name of database is Trip Database
let db = window.openDatabase('TripDatabase', '1.0', 'TripDatabase', 20000);

// Start device
$(document).on('ready', onDeviceReady);


// click menu button in page create trip to open navigation bar
$(document).on('click', '#page-create #icon-menu', function () {
    $('#page-create #panel').panel('open');
});
// click menu button in page list trip to open navigation bar
$(document).on('click', '#page-list #icon-menu', function () {
    $('#page-list #panel').panel('open');
});
// click menu button in page detail trip to open navigation bar
$(document).on('click', '#page-detail #icon-menu', function () {
    $('#page-detail #panel').panel('open');
});


// PAGE CREATE TRIP
// click create button in create page to call function confirmTrip() 
$(document).on('submit', '#page-create #frm-register', confirmTrip);
// click confirm button in confirm dialog to call function registerTrip() 
$(document).on('submit', '#page-create #frm-confirm', registerTrip);
// click edit button in confirm dialog to close confirm dialog 
$(document).on('click', '#page-create #frm-confirm #edit', function () {
    $('#page-create #frm-confirm').popup('close');
});

// PAGE LIST
// show empty trip list before add trips
$(document).on('pagebeforeshow', '#page-list', showList);
// click filter icon to call search() function
$(document).on('submit', '#page-list #frm-search', search);
// input to search field to call filterTrip() function
$(document).on('keyup', $('#page-list #txt-filter'), filterTrip);
// click reset icon to call showList() function
$(document).on('click', '#page-list #btn-reset', showList);
// click to search icon to call openFormSearch() function
$(document).on('click', '#page-list #btn-filter-popup', openFormSearch);
// click a link tag containing a trip on trip list to call navigatePageDetail() function
$(document).on('click', '#page-list #list-trip li a', navigatePageDetail);

// PAGE DETAIL
// click any trip on trip list to call showDetail() function
$(document).on('pagebeforeshow', '#page-detail', showDetail);
// click toggle expense icon on trip list 
$(document).on('click', '#page-detail #btn-toggle-expense', function () {
    // expenses list will be displayed
    let expenseDisplay = $('#page-detail #expense').css('display');
    $('#page-detail #expense').css('display', expenseDisplay == 'none' ? 'block' : 'none');
});
// click update button in 3 dots icon to call showUpdate() function
$(document).on('click', '#page-detail #btn-update-popup', showUpdate);
// click delete button in 3 dots icon to open delete dialog
$(document).on('click', '#page-detail #btn-delete-popup', function () {
    changePopup($('#page-detail #option'), $('#page-detail #frm-delete'));
});
// click cancel button in update dialog to exit update and close dialog
$(document).on('click', '#page-detail #frm-update #cancel', function () {
    $('#page-detail #frm-update').popup('close');
});
// click cancel button in add expense dialog to exit and close dialog
$(document).on('click', '#page-detail #frm-add-expense #cancel', function () {
    $('#page-detail #frm-add-expense').popup('close');
});
// click update button in update dialog to call updateTrip() function
$(document).on('submit', '#page-detail #frm-update', updateTrip);
// click delete button in delete dialog to call deleteTrip() function
$(document).on('submit', '#page-detail #frm-delete', deleteTrip);
// click add button in add expense dialog to call addExpense() function
$(document).on('submit', '#page-detail #frm-add-expense', addExpense);
// input to confirm deletefield to call confirmDeleteTrip() function
$(document).on('keyup', '#page-detail #frm-delete #txt-confirm', confirmDeleteTrip);

// database is ready to work
function onDeviceReady() {
    log(`Device is ready.`);

    prepareDatabase(db);
}

function log(message, type = 'INFO') {
    console.log(`${new Date()} [${type}] ${message}`);
}

// for another function call open or close dialogs
function changePopup(sourcePopup, destinationPopup) {
    let afterClose = function () {
        destinationPopup.popup("open");
        sourcePopup.off("popupafterclose", afterClose);
    };

    sourcePopup.on("popupafterclose", afterClose);
    sourcePopup.popup("close");
}

function confirmTrip(e) {
    e.preventDefault();

    log('Open the confirmation popup.');
    // The information entered on the Create Trip page will be loaded with their id in the Confirm dialog.
    $('#page-create #frm-confirm #name').text($('#page-create #frm-register #name').val());
    $('#page-create #frm-confirm #destination').text($('#page-create #frm-register #destination').val());
    $('#page-create #frm-confirm #date').text($('#page-create #frm-register #date').val());
    $('#page-create #frm-confirm #description').text($('#page-create #frm-register #description').val());
    $('#page-create #frm-confirm #risk').text($('#page-create #frm-register #risk').val());
    $('#page-create #frm-confirm #transportation').text($('#page-create #frm-register #transportation').val());
    $('#page-create #frm-confirm #upgrade').text($('#page-create #frm-register #upgrade').val());


    $('#page-create #frm-confirm').popup('open');
}

function registerTrip(e) {
    e.preventDefault();
    // assign information entered in input fields to variables
    let name = $('#page-create #frm-register #name').val();
    let destination = $('#page-create #frm-register #destination').val();
    let date = $('#page-create #frm-register #date').val();
    let description = $('#page-create #frm-register #description').val();
    let risk = $('#page-create #frm-register #risk').val();
    let transportation = $('#page-create #frm-register #transportation').val();
    let upgrade = $('#page-create #frm-register #upgrade').val();


    db.transaction(function (tx) {
        // Load variables into the corresponding columns of the Trip table in the database
        let query = `INSERT INTO Trip (Name, Destination, Description, Risk,Transportation, Upgrade, Date) VALUES (?, ?, ?, ?,?,?, julianday('${date}'))`;
        tx.executeSql(query, [name, destination, description, risk, transportation, upgrade], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            let message = `Added trip '${name}'.`;

            log(message);
            toast(message);

            $('#page-create #frm-register').trigger('reset');
            $('#page-create #frm-register #name').focus();
            // close confirm dialog
            $('#page-create #frm-confirm').popup('close');
        }
    });
}

function showList() {
    db.transaction(function (tx) {
        // Get all information in trip table
        let query = `SELECT *, date(Date) AS DateConverted FROM Trip`;

        tx.executeSql(query, [], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            log(`Get list of trips successfully.`);
            // display each trip into each row
            displayList(result.rows);
        }
    });
}

function navigatePageDetail(e) {
    e.preventDefault();
    // When the user clicks on any trip in the Trip List,
    //  get the value of that trip's id and assign it to a variable
    let id = $(this).data('details').Id;
    // save the value of that id along with the current location of that trip in the trip list
    localStorage.setItem(currentTripId, id);
    // redirect from a link tag containing a trip to the Trip detail page
    $.mobile.navigate('#page-detail', { transition: 'none' });
}

function showDetail() {
    // Get a link tag containing a trip and assign it a variable
    let id = localStorage.getItem(currentTripId);

    db.transaction(function (tx) {
        // Get all the information of a trip based on the trip ID assigned to a variable
        let query = `SELECT *, date(Date) AS DateConverted FROM Trip WHERE Id = ?`;

        tx.executeSql(query, [id], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            if (result.rows[0] != null) {
                log(`Get details of trip '${result.rows[0].name}' successfully.`);
                // pass the data of the columns of row [0] in the Trip table to the corresponding fields in the detail trip
                $('#page-detail #detail #name').text(result.rows[0].Name);
                $('#page-detail #detail #destination').text(result.rows[0].Destination);
                $('#page-detail #detail #description').text(result.rows[0].Description);
                $('#page-detail #detail #risk').text(result.rows[0].Risk);
                $('#page-detail #detail #transportation').text(result.rows[0].Transportation);
                $('#page-detail #detail #upgrade').text(result.rows[0].Upgrade);
                $('#page-detail #detail #date').text(result.rows[0].DateConverted);

                // call showExpense() function
                showExpense();
            }
            else {
                // Declare a variable containing message
                let errorMessage = 'Trip not found.';

                log(errorMessage, ERROR);
                // if there is no trip in trip list, it will display above message
                $('#page-detail #detail #name').text(errorMessage);
                // update button wil be disabled
                $('#page-detail #btn-update').addClass('ui-disabled');
                // delete dialog wil be disabled 
                $('#page-detail #btn-delete-confirm').addClass('ui-disabled');
            }
        }
    });
}

function confirmDeleteTrip() {
    // creates a variable containing whatever is entered
    let text = $('#page-detail #frm-delete #txt-confirm').val();
    // if text is confirm delete, then enable delete button
    if (text == 'confirm delete') {
        $('#page-detail #frm-delete #btn-delete').removeClass('ui-disabled');
    }
    // if text is not confirm delete, then still disable delete button
    else {
        $('#page-detail #frm-delete #btn-delete').addClass('ui-disabled');
    }
}

function deleteTrip(e) {
    e.preventDefault();
    // get the current position that the trip we want
    //  to delete is in the trip list and assign it a tripId variable
    let tripId = localStorage.getItem(currentTripId);

    // move the data of the trip we want to delete
    db.transaction(function (tx) {
        let name = '';
        // Get all the information of that trip in the database
        let query = 'SELECT * FROM Trip WHERE Id = ?';
        tx.executeSql(query, [tripId], function (tx, result) {
            name = result.rows[0].Name;
        }, transactionError);
        // delete all information of expenses of the trip being deleted
        query = 'DELETE FROM Expense WHERE TripId = ?';
        tx.executeSql(query, [tripId], function (tx, result) {
            log(`Delete expenses of trip '${tripId}' successfully.`);
        }, transactionError);
        // Delete the trip with the id corresponding to the selected current location
        query = 'DELETE FROM Trip WHERE Id = ?';
        tx.executeSql(query, [tripId], function (tx, result) {
            let message = `Deleted trip '${name}'.`;

            log(message);
            toast(message);
            // reset trip list after deleting that trip
            $('#page-detail #frm-delete').trigger('reset');
            // Navigate to the trip list page
            $.mobile.navigate('#page-list', { transition: 'none' });
        }, transactionError);
    });
}

function addExpense(e) {
    e.preventDefault();
    // assign each information in the input expense fields to the correspondingly named variables
    let tripId = localStorage.getItem(currentTripId);
    let type = $('#page-detail #frm-add-expense #type').val();
    let amount = parseInt($('#page-detail #frm-add-expense #amount').val());
    let date = $('#page-detail #frm-add-expense #date').val();
    let time = $('#page-detail #frm-add-expense #time').val();
    let comment = $('#page-detail #frm-add-expense #comment').val();

    db.transaction(function (tx) {
        // Save that information in the Expense table corresponding to each column
        let query = `INSERT INTO Expense (Type, Amount, Comment, TripId, Date, Time) VALUES (?, ?, ?, ?, julianday('${date}'), julianday('${time}'))`;
        tx.executeSql(query, [type, amount, comment, tripId], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            let message = `Added expense '${amount.toLocaleString("en-US")} VNĐ (${type})'.`;

            log(message);
            toast(message);
            // reset add expense dialog
            $('#page-detail #frm-add-expense').trigger('reset');
            // close add expense dialog
            $('#page-detail #frm-add-expense').popup('close');
            // call showExpense() function
            showExpense();
        }
    });
}

function showExpense() {
    // assign the current value of the trip id we want to add expense to the variable id
    let id = localStorage.getItem(currentTripId);

    db.transaction(function (tx) {
        // Get the expense information of that trip
        let query = `SELECT *, date(Date) AS DateConverted, time(Time) AS TimeConverted FROM Expense WHERE TripId = ? ORDER BY Date DESC, Time DESC`;

        tx.executeSql(query, [id], transactionSuccess, transactionError);
        // Display the information of that expense in the corresponding fields of that expense in Expense List
        function transactionSuccess(tx, result) {
            log(`Get list of expenses successfully.`);

            let expenseList = '';
            let currentDate = '';
            for (let expense of result.rows) {
                if (currentDate != expense.DateConverted) {
                    // display date of expense
                    expenseList += `<div style="text-align: center;height: 25px; background-color: #4F9068;color: white;line-height: 25px; margin-top:10px" >${expense.DateConverted}</div>`;
                    currentDate = expense.DateConverted;
                }
                // display expense type, amount, time, and comment
                expenseList += `
                    <div class='list'>
                        <table style='white-space: nowrap; width: 100%;'>
                            <tr style='height: 25px;'>
                                <td>${expense.Type}: ${expense.Amount.toLocaleString('en-US')} VNĐ</td>
                                <td style='text-align: right;'>${expense.TimeConverted}</td>
                            </tr>

                            <tr>
                                <td colspan='2'><em>${expense.Comment}</em></td>
                            </tr>
                        </table>
                    </div>`;
            }

            expenseList += `<div style="border: 1px solid #4F9068;border-radius: 5px;padding: 10px; margin-top: 20px;">You've reached the end of the list.</div>`;
            // empty expense list replaces by expenses 
            $('#page-detail #expense #list').empty().append(expenseList);

            log(`Show list of expenses successfully.`);
        }
    });
}

function showUpdate() {
    // get the current position that the trip we want
    //  to update is in the trip list and assign it a tripId variable
    let id = localStorage.getItem(currentTripId);

    db.transaction(function (tx) {
        // Get all the information of that trip in the database
        let query = `SELECT *, date(Date) as DateConverted FROM Trip WHERE Id = ?`;

        tx.executeSql(query, [id], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            if (result.rows[0] != null) {
                log(`Get details of trip '${result.rows[0].Name}' successfully.`);
                // The input fields in the update dialog are 
                // displayed with the old information saved to the database 
                // corresponding to the columns in the Trip table
                $(`#page-detail #frm-update #name`).val(result.rows[0].Name);
                $(`#page-detail #frm-update #destination`).val(result.rows[0].Destination);
                $(`#page-detail #frm-update #risk`).val(result.rows[0].Risk).slider("refresh");
                $(`#page-detail #frm-update #date`).val(result.rows[0].DateConverted);
                $(`#page-detail #frm-update #description`).val(result.rows[0].Description);
                // Allows closing or continuing operation in update dialog
                changePopup($('#page-detail #option'), $('#page-detail #frm-update'));
            }
        }
    });
}

function updateTrip(e) {
    e.preventDefault();
    // assign changed data in update dialog to variables
    let id = localStorage.getItem(currentTripId);
    let name = $('#page-detail #frm-update #name').val();
    let destination = $('#page-detail #frm-update #destination').val();
    let date = $('#page-detail #frm-update #date').val();
    let description = $('#page-detail #frm-update #description').val();
    let risk = $('#page-detail #frm-update #risk').val();

    db.transaction(function (tx) {
        // Save these changes to the database
        let query = `UPDATE Trip SET Name = ?, Destination = ?, Description = ?, Risk = ?, Date = julianday('${date}') WHERE Id = ?`;

        tx.executeSql(query, [name, destination, description, risk, id], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            let message = `Updated trip '${name}'.`;

            log(message);
            toast(message);
            // call showDetail() function
            showDetail();
            // close update dialog
            $('#page-detail #frm-update').popup('close');
        }
    });
}

function filterTrip() {
    // get name input from search field
    let filter = $('#page-list #txt-filter').val().toLowerCase();
    // take each trip in the trip list
    let li = $('#page-list #list-trip ul li');
    // for loop with variable i will go through all the trips and check the name of each trip
    for (let i = 0; i < li.length; i++) {
        let a = li[i].getElementsByTagName('a')[0];
        let text = a.textContent || a.innerText;
        // get the trip named corresponding to input and display
        li[i].style.display = text.toLowerCase().indexOf(filter) > -1 ? '' : 'none';
    }
}

function openFormSearch(e) {
    e.preventDefault();
    // Open search dialog
    $('#page-list #frm-search').popup('open');
}

function search(e) {
    e.preventDefault();
    // get name, destination, date inputed from search field
    let name = $('#page-list #frm-search #name').val();
    let destination = $('#page-list #frm-search #destination').val();
    let date = $('#page-list #frm-search #date').val();

    db.transaction(function (tx) {
        // get all trips in the database
        let query = `SELECT *, date(Date) as DateConverted FROM Trip WHERE`;
        // check if the trip on the trip matches the name, destination, date of any trip in the list
        query += name ? ` Name LIKE "%${name}%"   AND` : '';
        query += destination ? ` Destination LIKE "%${destination}%"   AND` : '';
        query += date ? ` Date = julianday('${date}')   AND` : '';

        query = query.substring(0, query.length - 6);

        tx.executeSql(query, [], transactionSuccess, transactionError);

        function transactionSuccess(tx, result) {
            log(`Search trips successfully.`);
            // call display() function
            displayList(result.rows);

            $('#page-list #frm-search').trigger('reset');
            $('#page-list #frm-search').popup('close');
        }
    });
}

function displayList(list) {
    let tripList = `<ul id='list-trip' data-role='listview' class='ui-nodisc-icon ui-alt-icon'>`;
    // Display a message when no matching trip is found
    tripList += list.length == 0 ? '<li><h2>There is no trip.</h2></li>' : '';
    // Show trip results found in trip list page
    for (let trip of list) {
        tripList +=
            `<li><a data-details='{"Id" : ${trip.Id}}'>
                <h6>${trip.Name}</h6>
                <p><small>${trip.DateConverted} - <em>${trip.Destination}</em></small></p>
            </a></li>`;
    }
    tripList += `</ul>`;

    $('#list-trip').empty().append(tripList).listview('refresh').trigger('create');

    log(`Show list of trips successfully.`);
}

function toast(message) {
    // Display messages when an error occurs
    $("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><p>" + message + "</p></div>")
        .css({
            background: '#EF9175',
            color: 'black',
            font: '12px Arial, sans-serif',
            display: 'block',
            opacity: 1,
            position: 'fixed',
            padding: '2px',
            'border-radius': '25px',
            'text-align': 'center',
            'box-shadow': 'none',
            '-moz-box-shadow': 'none',
            '-webkit-box-shadow': 'none',
            width: '250px',
            left: ($(window).width() - 254) / 2,
            top: $(window).height() - 115
        })

        .appendTo($.mobile.pageContainer).delay(3500)

        .fadeOut(400, function () {
            $(this).remove();
        });
}