package database.main;

import couponsystem.CouponSystemException;
import databaseMain.DatabaseInfrastructure;

public class DropDBOnly {

	public static void main(String[] args) throws CouponSystemException {
		
		DatabaseInfrastructure db = new DatabaseInfrastructure();
		if (db.dropDatabase() == true) {
			System.out.println(db.getDatabaseName() + " database deleted");
		}
	}

}
