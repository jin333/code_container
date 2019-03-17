/**
	needed install : mysql
	TEST Purpose : make some connection with DB
	
*/
var mysql = require('mysql');
const dbconfig = require('./dbconfig.js');
/*
	./dbconfig.js
	
	module.exports = {
			host: 'localhost',
			user: 'userid',
			password: 'password',
			port: port_num,
			database: 'database_name'
	}
*/

var connection = mysql.createConnection({
    host     : dbconfig.host,
    user     : dbconfig.user,
    password : dbconfig.password,
    port     : dbconfig.port,
    database : dbconfig.database
});

connection.connect();

var result  = connection.query('SELECT * from sale_info where  Product_code = \'AA006\'', function(err, rows, fields) {
    if (!err)
        console.log('The solution is: ', rows[1]);
    else
        console.log('Error while performing Query.', err);
});

connection.end();

console.log('test'+result[1])