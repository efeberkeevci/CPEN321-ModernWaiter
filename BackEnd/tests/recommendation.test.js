const app = require('../backend_server')
const supertest = require('supertest')
const request = supertest(app)
const recommendation_logic = require('../recommendation_logic')

describe('getItemRecommendation()', () => {
    test('Should 200', () => {
      const req = mockRequest({ userId : "1", tableId : "1", restaurantId : "1", amount : "55.43", hasPaid : "0", isActive : "1"})
      const res = mockResponse();

      // Arrange
      const mock = jest.fn().mockReturnValue({itemId : 1})
      recommendation_logic.getRecommendation = mock

      // Act
      const response = await request.get('/recommendation/1/1')

      // Assert
      expect(response.status).toBe(200)
    });
  });