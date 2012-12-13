package com.service.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.service.models.User;
import com.utils.wsutils.ServiceException;


public class MongoDBUserDaoImpl  implements UserDao {

	private MongoTemplate mongoTemplate;
	
	@Override
	public void add(Object object) throws ServiceException {
		mongoTemplate.save(object);
	}

	@Override
	public void update(String username, Map<String,Object> changes) throws ServiceException {
		Query query = new Query(Criteria.where("username").is(username));
		for (Map.Entry<String, Object> m :changes.entrySet()){
			WriteResult result = mongoTemplate.updateMulti(query,Update.update(m.getKey(), m.getValue()), "user");
			if (!result.getLastError().ok()) throw new ServiceException(result.getError());
		}
	}

	@Override
	public void delete(String username) throws ServiceException {
		mongoTemplate.remove(new Query(
                Criteria.where("username").is(username)
            ), "user");
	}
	
	@Override
	public void delete(User user) throws ServiceException {
		mongoTemplate.remove(user);
	}

	@Override
	public User get(String username) throws ServiceException {
		User user = mongoTemplate.findOne(
				new Query(Criteria.where("username").is(username)
		                ), User.class,"user"
		          );
		return user;
	}

	@Override
	public User login(String username, String password) throws ServiceException {
		User user = mongoTemplate.findOne(
				new Query(Criteria.where("username").is(username).and("password").is(password)
		                ), User.class,"user"
		          );
		if (user == null) throw new ServiceException("user not found");
		return user;
	}

	@Override
	public User get(BigInteger id) throws ServiceException {
		User user = mongoTemplate.findOne(
				new Query(Criteria.where("id").is(id)
		                ), User.class,"user"
		          );
		if (user == null) throw new ServiceException("user not found");
		return user;
	}
	
	@Override
	public User getBySocial(String service, String key, String value) throws ServiceException {
		User user = mongoTemplate.findOne(
				new Query(Criteria.where("services."+service+"."+key).is(value)
		                ), User.class,"user"
		          );
		if (user == null) throw new ServiceException("user not found");
		return user;
	}


	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<User> list() throws ServiceException {
		return mongoTemplate.findAll(User.class);
	}

	@Override
	public void update(User user) throws ServiceException {
		mongoTemplate.save(user);
		
	}

	

	
	
	
}
