const UsersController = require('../controllers/usersController');

module.exports = (app) => {

    // GET
    app.get('/api/users/getAll', UsersController.getAll);
    
    // POST - SAVE DATA
    app.post('/api/users/create', UsersController.register);

}
