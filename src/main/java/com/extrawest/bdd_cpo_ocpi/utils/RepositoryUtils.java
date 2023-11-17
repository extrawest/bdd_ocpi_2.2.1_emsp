package com.extrawest.bdd_cpo_ocpi.utils;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.springframework.data.mongodb.CodecRegistryProvider;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class RepositoryUtils {

    public static BulkWriteResult importToCollection(
            CodecRegistryProvider codecRegistryProvider,
            MongoTemplate template, String jsonArray, String collectionName) {
        DecoderContext decoderContext = DecoderContext.builder().build();
        Codec<Document> documentCodec = codecRegistryProvider.getCodecFor(Document.class)
                .orElseThrow(() -> new RuntimeException(
                        "Failed to load entities from json file"));
        List<InsertOneModel<Document>> bulk = BsonArray.parse(jsonArray)
                .stream()
                .map(bsonValue -> documentCodec.decode(
                        bsonValue.asDocument().asBsonReader(), decoderContext))
                .map(InsertOneModel::new)
                .collect(toList());
        MongoCollection<Document> collection = template.getCollection(collectionName);
        return collection.bulkWrite(bulk);
    }

    public static DeleteResult remove(MongoTemplate template, Query query, String collection) {
        return template.remove(query, collection);
    }
}