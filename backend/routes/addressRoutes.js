const AddressController = require('../controllers/addressController');
const passport = require('passport');

module.exports = (app) => {

    // GET
    app.get('/api/address/findByUser/:id_user', passport.authenticate('jwt', {session: false}), AddressController.findByUser);
    
    // POST - SAVE DATA
    app.post('/api/address/create', passport.authenticate('jwt', {session: false}), AddressController.create);

}
