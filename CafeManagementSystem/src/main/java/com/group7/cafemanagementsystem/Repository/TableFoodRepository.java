package com.group7.cafemanagementsystem.Repository;
import com.group7.cafemanagementsystem.model.TableFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TableFoodRepository extends JpaRepository<TableFood, Integer> {

}
