const UsersController = require('../controllers/usersController');
const passport = require('passport');

module.exports = (app, upload) => {

    // GET
    app.get('/api/users/getAll', UsersController.getAll);
    app.get('/api/users/findDeliveryMan', passport.authenticate('jwt', {session: false}), UsersController.findByDeliveryMan);
    
    // POST - SAVE DATA
    app.post('/api/users/create', UsersController.register);
    app.post('/api/users/login', UsersController.login);

    // PUT - UPDATE DATA
    // passport.authenticate('jwt', {session: false}) - is to autenticate the user with his token
    // 401 UNAUTHORIZED

    app.put('/api/users/update', passport.authenticate('jwt', {session: false}), upload.array('image', 1), UsersController.update);
    app.put('/api/users/updateWithoutImage', passport.authenticate('jwt', {session: false}), UsersController.updateWithoutImage);


}
