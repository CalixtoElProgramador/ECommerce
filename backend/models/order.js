const db = require('../config/config');

const Order = {};

Order.findByClientAndStatus = (id_client, status) => {
    const sql = `
    SELECT
        O.id,
        O.id_client,
        O.id_address,
        O.id_delivery,
        O.status,
        O.lat,
        O.lng,
        O.timestamp,
        JSON_AGG(
            JSON_BUILD_OBJECT(
                'id', P.id,
                'name', P.name,
                'description', P.description,
                'price', P.price,
                'image00', P.image00,
                'image01', P.image01,
                'image02', P.image02,
                'quantity', OHP.quantity
            )
        ) AS products,
        JSON_BUILD_OBJECT(
            'id', U.id,
            'name', U.name,
            'lastname', U.lastname,
            'image', U.image
        ) AS client,
        JSON_BUILD_OBJECT(
            'id', A.id,
            'address', A.address,
            'neighborhood', A.neighborhood,
            'lat', A.lat,
            'lng', A.lng
        ) AS address
    FROM
        orders AS O
    INNER JOIN
        users AS U
    ON
        O.id_client = U.id
    INNER JOIN
        address AS A
    ON
        O.id_address = A.id
    INNER JOIN
        order_has_products AS OHP
    ON
        OHP.id_order = O.id
    INNER JOIN
        products AS P
    ON
        P.id = OHP.id_product
    WHERE
        O.id_client = $1
    AND
        O.status = $2
    GROUP BY
        O.id, U.id, A.id
    ORDER BY
        O.timestamp
    DESC
    `;

    return db.manyOrNone(sql, [
        id_client,
        status
    ]);

};

Order.findByStatus = (status) => {
    const sql = `
    SELECT
        O.id,
        O.id_client,
        O.id_address,
        O.id_delivery,
        O.status,
        O.lat,
        O.lng,
        O.timestamp,
        JSON_AGG(
            JSON_BUILD_OBJECT(
                'id', P.id,
                'name', P.name,
                'description', P.description,
                'price', P.price,
                'image00', P.image00,
                'image01', P.image01,
                'image02', P.image02,
                'quantity', OHP.quantity
            )
        ) AS products,
        JSON_BUILD_OBJECT(
            'id', U.id,
            'name', U.name,
            'lastname', U.lastname,
            'image', U.image
        ) AS client,
        JSON_BUILD_OBJECT(
            'id', A.id,
            'address', A.address,
            'neighborhood', A.neighborhood,
            'lat', A.lat,
            'lng', A.lng
        ) AS address
    FROM
        orders AS O
    INNER JOIN
        users AS U
    ON
        O.id_client = U.id
    INNER JOIN
        address AS A
    ON
        O.id_address = A.id
    INNER JOIN
        order_has_products AS OHP
    ON
        OHP.id_order = O.id
    INNER JOIN
        products AS P
    ON
        P.id = OHP.id_product
    WHERE
        O.status = $1
    GROUP BY
        O.id, U.id, A.id
    ORDER BY
        O.timestamp
    DESC
    `;

    return db.manyOrNone(sql, status);

};

Order.create = (order) => {
    const sql = `
    INSERT INTO
        orders(
            id_client,
            id_address,
            status,
            timestamp,
            created_at,
            updated_at
        )
    VALUES($1, $2, $3, $4, $5, $6) RETURNING id
    `;

    return db.oneOrNone(sql, [
        order.id_client,
        order.id_address,
        order.status,
        Date.now(),
        new Date(),
        new Date()
    ]);

};

module.exports = Order