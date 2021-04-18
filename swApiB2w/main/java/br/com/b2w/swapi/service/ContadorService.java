package br.com.b2w.swapi.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import br.com.b2w.swapi.model.Contador;

@Service
public class ContadorService {

    @Autowired
    private MongoOperations mongoOperations;

    public long getNextSequence(String collectionName) {

        Contador contador = mongoOperations.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                Contador.class);

        return contador.getSeq();
    }

    public boolean collectionExists(String collectionName) {
        return mongoOperations.collectionExists(collectionName);
    }

    public void createCollection() {
        Contador contador = new Contador();
        contador.setSeq(0);
        contador.setId("Contador");
        mongoOperations.insert(contador);
    }
    
}
