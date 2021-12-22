const UsersController = require('../controllers/usersController');

module.exports = (app, upload) => {

    // GET
    app.get('/api/users/getAll', UsersController.getAll);
    
    // POST - SAVE DATA
    app.post('/api/users/create', UsersController.register);
    app.post('/api/users/login', UsersController.login);

    // PUT - UPDATE DATA
    app.put('/api/users/update', upload.array('image', 1), UsersController.update);
    app.put('/api/users/updateWithoutImage', UsersController.updateWithoutImage);

}
