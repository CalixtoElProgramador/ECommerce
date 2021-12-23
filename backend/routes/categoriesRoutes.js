const CategoriesController = require('../controllers/categoriesController');
const passport = require('passport');

module.exports = (app, upload) => {

    // GET
    app.get('/api/categories/getAll', passport.authenticate('jwt', {session: false}), CategoriesController.getAll);
    
    // POST - SAVE DATA
    app.post('/api/categories/create', passport.authenticate('jwt', {session: false}), upload.array('image', 1), CategoriesController.create);

    // PUT - UPDATE DATA
    // passport.authenticate('jwt', {session: false}) - is to autenticate the user with his token
    // 401 UNAUTHORIZED

}
