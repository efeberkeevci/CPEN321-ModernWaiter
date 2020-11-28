const { testCreateUser, testGetUserByGoogleId, testGetUserByUserId, testUpdateUserPreferences, testGetUserPreferences } = require('./test_functions')

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




