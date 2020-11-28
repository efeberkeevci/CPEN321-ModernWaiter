const app = require('../../backend_server')
const supertest = require('supertest')
const request = supertest(app)
const { testGetMenu } = require('./test_functions')

//Test to get restaurant, get restaurant menu, 

describe("Integration test 6: ", () => {
    it("Test to add a restaurant, verify added restaurant, get restaurant menu", async done => {
        await testAddRestaurant()
        await testGetRestaurant()
        await testGetMenu()
        done()
    })
})

async function testGetRestaurant() {
    // Arrange
    const url = `/restaurants/2`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    /*
    expect(response.body[response.body.length-1].location).toStrictEqual(location)
    expect(response.body[response.body.length-1].name).toStrictEqual(name)
    expect(response.body[response.body.length-1].tax_percentage).toStrictEqual(tax_percentage)
    expect(String(response.body[response.body.length-1].service_fee_percentage)).toStrictEqual(String(service_fee_percentage))
    */
}

async function testAddRestaurant() {
    // Arrange
    const url = `/restaurants`
    const req_body = {
        "taxPercentage" : "12",
        "serviceFeePercentage": "0",
        "name" : "Best Restaurant",
        "location" : "Calgary"
    }

    // Act
    const response = await request.post(url).send(req_body)

    // Assert
    expect(response.status).toBe(200)
    restaurantId = response.body.id
}


