package model;

import java.util.ArrayList;
import java.util.Objects;

public class DatabaseInfo {
    private ArrayList<CollectionInfo> collections;

    public DatabaseInfo() {
        CollectionInfo collection;
        AttributeInfo attribute;

        ArrayList<AttributeInfo> attributes;
        collections = new ArrayList<>();

        // Collection BAIRRO:
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("BAIRRO");

        attribute = new AttributeInfo();
        attribute.setName("NOME");
        attribute.setRealName("_id.NOME");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOMECIDADE");
        attribute.setRealName("_id.NOMECIDADE");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("SIGLAESTADO");
        attribute.setRealName("_id.SIGLAESTADO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("CEP");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROZONA");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection BAIRRO

        // Collection CANDIDATO
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("CANDIDATO");

        attribute = new AttributeInfo();
        attribute.setName("NROCAND");
        attribute.setRealName("_id.NROCAND");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("TIPO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("CPF");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOME");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("IDADE");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("APELIDO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("SIGLA");
        attribute.setRealName("PARTIDO.SIGLA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOMEPARTIDO");
        attribute.setRealName("PARTIDO.NOME");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection CANDIDATO

        // Collection CANDIDATURA
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("CANDIDATURA");

        attribute = new AttributeInfo();
        attribute.setName("REG");
        attribute.setRealName("_id.REG");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("CODCARGO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ANO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROCAND");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROVICE");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROSESSAO");
        attribute.setRealName("SESSAO.NROSESSAO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROZONA");
        attribute.setRealName("SESSAO.NROZONA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("TOTALVOTOS");
        attribute.setRealName("SESSAO.TOTAL");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("REGPESQUISA");
        attribute.setRealName("PESQUISA.REGPESQUISA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("TOTALINTENCAO");
        attribute.setRealName("PESQUISA.TOTAL");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection CANDIDATURA

        // Collection CARGO
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("CARGO");

        attribute = new AttributeInfo();
        attribute.setName("CODCARGO");
        attribute.setRealName("_id.CODCARGO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("POSSUIVICE");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ANOSCARGO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ANOSMANDATO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOMEDESCRITIVO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NRODECADEIRAS");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ESFERA");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOMECIDADE");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("SIGLAESTADO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("SALARIO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection CARGO

        // Collection CIDADE
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("CIDADE");

        attribute = new AttributeInfo();
        attribute.setName("NOME");
        attribute.setRealName("_id.NOME");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("SIGLAESTADO");
        attribute.setRealName("_id.SIGLAESTADO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("POPULACAO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection CIDADE

        // Collection ESTADO
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("ESTADO");

        attribute = new AttributeInfo();
        attribute.setName("SIGLA");
        attribute.setRealName("_id.SIGLA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NOME");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection ESTADO

        // Collection PESQUISA
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("PESQUISA");

        attribute = new AttributeInfo();
        attribute.setName("REGPESQUISA");
        attribute.setRealName("_id.REGPESQUISA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("PERIODOINICIO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("PERIODOFIM");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ORGAOPESQUISA");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("REGCANDID");
        attribute.setRealName("CANDIDATURA.REGCANDID");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("TOTAL");
        attribute.setRealName("CANDIDATURA.TOTAL");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection PESQUISA

        // Collection SESSAO
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("SESSAO");

        attribute = new AttributeInfo();
        attribute.setName("NROSESSAO");
        attribute.setRealName("_id.NROSESSAO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROZONA");
        attribute.setRealName("_id.NROZONA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NSERIAL");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("CODCARGO");
        attribute.setRealName("CANDIDATURA.CODCARGO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ANO");
        attribute.setRealName("CANDIDATURA.ANO");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROCAND");
        attribute.setRealName("CANDIDATURA.NROCAND");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("TOTAL");
        attribute.setRealName("CANDIDATURA.TOTAL");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection SESSAO

        // Collection URNA
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("URNA");

        attribute = new AttributeInfo();
        attribute.setName("NSERIAL");
        attribute.setRealName("_id.NSERIAL");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("ESTADO");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NROZONA");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection URNA

        // Collection ZONA
        collection = new CollectionInfo();
        attributes = new ArrayList<>();
        collection.setName("ZONA");

        attribute = new AttributeInfo();
        attribute.setName("NROZONA");
        attribute.setRealName("_id.NROZONA");
        attribute.setEmbedded(true);
        attributes.add(attribute);

        attribute = new AttributeInfo();
        attribute.setName("NRODEURNASRESERVAS");
        attribute.setEmbedded(false);
        attributes.add(attribute);

        collection.setAttributes(attributes);
        collections.add(collection);
        // End Collection ZONA
    }

    public CollectionInfo getCollection(String collectionName) {
        for(CollectionInfo collection : collections) {
            if(Objects.equals(collection.getName(), collectionName)) {
                return collection;
            }
        }
        return null;
    }

    public ArrayList<AttributeInfo> getCollectionAttributes(String collectionName) {
        for(CollectionInfo collection : collections) {
            if(Objects.equals(collection.getName(), collectionName)) {
                return collection.getAttributes();
            }
        }
        return null;
    }

    public ArrayList<String> getCollectionNames() {
        ArrayList<String> names = new ArrayList<>();
        for(CollectionInfo collection : collections) {
            names.add(collection.getName());
        }
        return names;
    }
}
