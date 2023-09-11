package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.Address_;
import com.andriibashuk.userauthservice.entity.User;
import com.andriibashuk.userauthservice.entity.User_;
import com.andriibashuk.userauthservice.response.UserResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class CustomUserRepositoryImpl implements CustomUserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getAll(Integer id)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if(id != 0) {
            predicates.add(criteriaBuilder.equal(root.get(User_.id), id));
        }

        Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));


        query.where(finalPredicate);

        List<User> result =  entityManager.createQuery(query).setMaxResults(1).getResultList();

        return result;
    }
}
