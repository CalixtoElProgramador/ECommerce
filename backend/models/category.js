const db = require('../config/config');
const Category = {};

Category.create = (category) => {

    const sql = `
    INSERT INTO
        categories(
            name,
            image,
            created_at,
            updated_at
        )
    VALUES($1, $2, $3, $4) RETURNING id
    `;

    return db.oneOrNone(sql, [
        category.name,
        category.image,
        new Date(),
        new Date()
    ]);
}

module.exports = Category;