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
}
