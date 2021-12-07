package com.northeastern.edu.Facade;

import java.util.Optional;

import com.northeastern.edu.PersonRepository;
import com.northeastern.edu.SellerRepository;
import com.northeastern.edu.UserRepository;
import com.northeastern.edu.models.Seller;
import com.northeastern.edu.models.User;
import com.northeastern.edu.projections.SellerView;

import org.springframework.stereotype.Component;

@Component("userFacade")
public class DBUserFacade {
 
    private UserRepository userRepository;
    private SellerRepository sellerRepository;
    private PersonRepository personRepository;
    
    public DBUserFacade(UserRepository userRepository, SellerRepository sellerRepository,
            PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.sellerRepository = sellerRepository;
        this.personRepository = personRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Seller saveSeller(Seller seller){
        return sellerRepository.save(seller);
    }

    public Seller findSellerByEmail(String email){
        return sellerRepository.findByEmail(email);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public Optional<SellerView> findSellerByIdForLimitedData(int id){
        return sellerRepository.findByIdForLimitedData(id);
    }

    public Optional<SellerView> findSellerByEmailForLimitedData(String email){
        return sellerRepository.findByEmailForLimitedData(email);
    } 

    public void deleteSeller(Seller seller){
        sellerRepository.delete(seller);
    }

    public Optional<User> findUserById(int id){
        return userRepository.findById(id);
    }

    public Optional<Seller> findSellerById(int id){
        return sellerRepository.findById(id);
    }


}
