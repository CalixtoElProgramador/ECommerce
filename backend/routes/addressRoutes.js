const AddressController = require('../controllers/addressController');
const passport = require('passport');

module.exports = (app) => {
    
    // POST - SAVE DATA
    app.post('/api/address/create', passport.authenticate('jwt', {session: false}), AddressController.create);

}
