package com.ugle.ugle.repositories;

import com.ugle.ugle.entities.WebPage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SearchRepositoryImp implements SearchRepository{
    //Carga conexion a base de datos.
    @PersistenceContext
    EntityManager entityManager;
    @Transactional
    @Override
    public List<WebPage> search(String textSearch) {
        String query = "FROM WebPage WHERE description like :textSearch";
        //Esto no es sql eso no pongo SELECT FROM
        // es el sql de hibernate de hibernate-> es el puente intermedio entra la BD y el proyecto
        // le paso la clase WebPage no el nombre de la tabla
        //Envio la consulta, me traigo la lista de resultados
        return entityManager.createQuery(query)
                .setParameter("textSearch","%"+textSearch +"%")
                .getResultList();
                //reemplazo el parametro.
    }

    @Transactional
    @Override
    public void save(WebPage webPage) {
    entityManager.merge(webPage);
    }
    @Override
    public WebPage getByUrl(String url){
        String query = "FROM WebPage WHERE url= :url";
        List<WebPage> list = entityManager.createQuery(query)
                .setParameter("url",url)
                .getResultList();
        return list.size() ==0 ? null:list.get(0);
    }

    @Override
    public List<WebPage> getLinksToIndex() {
        String query = "FROM WebPage WHERE title is null AND description is null";
        return entityManager.createQuery(query)
                .setMaxResults(100)
                .getResultList();
    }

    @Override
    public boolean exist(String url) {
        return getByUrl(url) != null;
    }
}
