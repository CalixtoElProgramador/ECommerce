const Order = require('../models/order')
const OrderHasProducts = require('../models/order_has_prodcuts');
const timeRelative = require('../utils/time_relative');

module.exports = {

    async findByClientAndStatus(req, res, next) {
        try {

            const id_client = req.params.id_client;
            const status = req.params.status;
            let data = await Order.findByClientAndStatus(id_client, status)

            /** 
             * This small cycle is to change the timestamp of each of the commands to 
             * something much more readable and normal for a person.  
             */

            data.forEach(d => {
                d.timestamp = timeRelative(new Date().getTime(), d.timestamp);
            });

            // console.log('Order: ', data);

            return res.status(201).json(data);

        } catch (error) {

            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at obtain the orders by status and id_client',
                error: error
            });

        }
    },

    async findByDeliveryAndStatus(req, res, next) {
        try {

            const id_delivery = req.params.id_delivery;
            const status = req.params.status;
            let data = await Order.findByDeliveryAndStatus(id_delivery, status)

            /** 
             * This small cycle is to change the timestamp of each of the commands to 
             * something much more readable and normal for a person.  
             */

            data.forEach(d => {
                d.timestamp = timeRelative(new Date().getTime(), d.timestamp);
            });

            // console.log('Order: ', data);

            return res.status(201).json(data);

        } catch (error) {

            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at obtain the orders by status and id_delivery',
                error: error
            });

        }
    },

    async findByStatus(req, res, next) {
        try {

            const status = req.params.status;
            let data = await Order.findByStatus(status);

            data.forEach(d => {
                d.timestamp = timeRelative(new Date().getTime(), d.timestamp);
            });

            return res.status(201).json(data);

        } catch (error) {

            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at obtain the orders by status',
                error: error
            });

        }
    },

    async create(req, res, next) {
        try {

            const order = req.body;
            const data = await Order.create(order);

            /** 
             * This method is to save each product in the table "order_has_products".
             * The user need to pass like parameter an array of products. Each product
             * into the array needs his id and the quantity of this item.
             */
            for (const product of order.products) {
                await OrderHasProducts.create(data.id, product.id, product.quantity);
            }
            
            return res.status(201).json({
                success: true,
                message: 'The order was created successfully',
                data: {
                    'id': data.id
                }
            });

        } catch (error) {

            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at create the order',
                error: error
            });

        }
    },

    async updateToDispatched(req, res, next) {
        try {
            let order = req.body;
            order.status = 'DISPATCHED';
            await Order.update(order);
            
            return res.status(201).json({
                success: true,
                message: 'The order was updated successfully [DISPATCHED]'
            });

        } catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at uptade the order',
                error: error
            });

        }
    },

    async updateToOnTheWay(req, res, next) {
        try {
            let order = req.body;
            order.status = 'ON THE WAY';
            await Order.update(order);
            
            return res.status(201).json({
                success: true,
                message: 'The order was updated successfully [ON THE WAY]'
            });

        } catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at uptade the order',
                error: error
            });

        }
    },

    async updateToDelivered(req, res, next) {
        try {
            let order = req.body;
            order.status = 'DELIVERED';
            await Order.update(order);
            
            return res.status(201).json({
                success: true,
                message: 'The order was updated successfully [DELIVERED]'
            });

        } catch (error) {
            console.log(`Error: ${error}`);
            return res.status(501).json({
                success: false,
                message: 'An error was happened at uptade the order',
                error: error
            });

        }
    }

}