const OrdersController = require('../controllers/ordersController');
const passport = require('passport');

module.exports = (app) => {

    // GET
    app.get('/api/orders/findByStatus/:status', passport.authenticate('jwt', {session: false}), OrdersController.findByStatus);

    // POST - SAVE DATA
    app.post('/api/orders/create', passport.authenticate('jwt', {session: false}), OrdersController.create);

}
