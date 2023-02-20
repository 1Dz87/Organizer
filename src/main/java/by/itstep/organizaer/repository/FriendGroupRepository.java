package by.itstep.organizaer.repository;

import by.itstep.organizaer.model.entity.FriendGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendGroupRepository extends JpaRepository<FriendGroup,Long> {

}
