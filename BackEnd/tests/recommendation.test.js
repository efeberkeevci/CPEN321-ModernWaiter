const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)
const recommendation_logic = require('../recommendation_logic')

describe('Test getItemRecommendation() with mock recommendation logic', () => {
    it('Gets the recommended item with itemId 1', async done => {
        // Arrange
        const mock = jest.fn().mockReturnValue(1)
        recommendation_logic.getRecommendation = mock

        // Act
        const response = await request.get('/recommendation/1/1')

        // Assert
        expect(response.status).toBe(200)
        expect(response.body).toStrictEqual({itemId : 1})
        done()
      })
  })

  
