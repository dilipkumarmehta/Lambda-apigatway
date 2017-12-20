package com.amazonaws.lambda.demo;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<PersonRequest, PersonResponse> {
	private DynamoDB dynamoDb;
	private String DYNAMODB_TABLE_NAME = "Person";
	private Regions REGION = Regions.US_WEST_2;

	public PersonResponse handleRequest(PersonRequest personRequest, Context context) {
		this.initDynamoDbClient();
		persistData(personRequest);
		PersonResponse personResponse = new PersonResponse();
		personResponse.setMessage("Saved Successfully!!!");
		
		return personResponse;
	}

	private PutItemOutcome persistData(PersonRequest person) throws ConditionalCheckFailedException {

		Table table = dynamoDb.getTable("Person");
		Item item1 = new Item().withPrimaryKey("userId", person.getUserId())
				.withString("userName", person.getUserName()).withString("location", person.getLocation())
				.withString("primarySkill", person.getPrimarySkill())
				.withString("secondarySkill", person.getSecondarySkill())
				.withString("secondarySkill", person.getSecondarySkill())
				.withString("experience", person.getExperience()).withString("education", person.getEducation());
		PutItemOutcome outcome = table.putItem(item1);
		return outcome;
	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
		this.dynamoDb = new DynamoDB(client);
	}
}
