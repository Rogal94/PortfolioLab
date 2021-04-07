package pl.coderslab.charity.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository <Donation, Long> {
    @Query("SELECT SUM(d.quantity) FROM Donation d")
    Integer selectTotalsQuantity();
    @Query("SELECT COUNT(d.id) FROM Donation d")
    Integer selectTotalsDonation();
}
