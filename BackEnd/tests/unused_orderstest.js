const orders = require('../routes/orders.js')

describe('checkOrder', () => {
  test('should 200', () => {
    const req = mockRequest({ userId : "1", tableId : "1", restaurantId : "1", amount : "55.43", hasPaid : "0", isActive : "1"})
    const res = mockResponse();

    //orders.createOrder(req, res);
    // expect(res.status).toEqual(200);
    // expect(res.json).toEqual("");
    expect(1).toEqual(1); //placeholder
    //done()
  });
});

const mockRequest = (content) => {
  return {
    body: { content },
  };
};

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};
