const app = require('../../backend_server')
const supertest = require('supertest')
const request = supertest(app)
const { testGetMenu } = require('./test_functions')

var userId
var username
var email
var googleId
var preferences

describe("Integration test 7: ", () => {
    it("Set up a new user", async done => {

        timestamp = new Date().getTime()
        username = "Integration_test" + timestamp
        googleId = "dummy_google" + timestamp
        email = "integration_test_user" + timestamp + "@gmail.com"
        preferences = "chicken"

        await testCreateUser()
        await testGetUserByGoogleId()
        await testGetUserByUserId()
        await testGetUserPreferences()
        await testUpdateUserPreferences()
        await testGetUserPreferences()
        done()
    })
})

async function testCreateUser() {
    // Arrange
    const url = `/users`
    const req_body = 
        {
            "username" : username,
            "email" : email,
            "googleId" : googleId,
            "preferences" : preferences
        }

    // Act
    const response = await request.post(url).send(req_body)

    // Assert
    expect(response.status).toBe(200)
}

async function testGetUserByGoogleId() {
    // Arrange
    const url = `/users/google/${googleId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body.username).toStrictEqual(username)
    expect(response.body.email).toStrictEqual(email)
    expect(response.body.google_id).toStrictEqual(googleId)
    expect(response.body.preferences).toStrictEqual(preferences)
    expect(response.body.username).toStrictEqual(username)

    userId = response.body.id
}

async function testGetUserByUserId() {
    // Arrange
    const url = `/users/${userId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body.username).toStrictEqual(username)
    expect(response.body.email).toStrictEqual(email)
    expect(response.body.google_id).toStrictEqual(googleId)
    expect(response.body.preferences).toStrictEqual(preferences)
    expect(response.body.username).toStrictEqual(username)
}

async function testGetUserPreferences() {
    // Arrange
    const url = `/users/preferences/${userId}`

    // Act
    const response = await request.get(url)

    // Assert
    expect(response.status).toBe(200)
    expect(response.body.preferences).toStrictEqual(preferences)
}

async function testUpdateUserPreferences() {
    // Arrange
    const url = `/users/preferences/`
    const req_body = 
        {
            "userId" : userId,
            "preferences" : "mango cucumber tuna spicy"
        }

    // Act
    const response = await request.put(url).send(req_body)

    // Assert
    expect(response.status).toBe(200)
    preferences = "mango cucumber tuna spicy"
}


