package database;

import static database.Constants.Tables.*;

/**
 * Inspired by Alex's implementation
 */
public class SQLTableCreationFactory {

    public String getCreateSQLForTable(String table) {
        switch (table) {
            case CLIENT:
                return "CREATE TABLE IF NOT EXISTS client (" +
                        "  id int(11) NOT NULL AUTO_INCREMENT," +
                        "  name varchar(500) NOT NULL," +
                        "  address varchar(500) NOT NULL," +
                        "  card_nr varchar(500) NOT NULL," +
                        "  cnp char(10) NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE KEY id_UNIQUE (id)," +
                        "  UNIQUE INDEX cnp_UNIQUE (cnp ASC) "+
                        ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

            case USER:
                return "CREATE TABLE IF NOT EXISTS user (" +
                        "  id INT NOT NULL AUTO_INCREMENT," +
                        "  username VARCHAR(200) NOT NULL," +
                        "  password VARCHAR(64) NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  UNIQUE INDEX username_UNIQUE (username ASC));";

            case ROLE:
                return "  CREATE TABLE IF NOT EXISTS role (" +
                        "  id INT NOT NULL AUTO_INCREMENT," +
                        "  role VARCHAR(100) NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  UNIQUE INDEX role_UNIQUE (role ASC));";

            case RIGHT:
                return "  CREATE TABLE IF NOT EXISTS `right` (" +
                        "  `id` INT NOT NULL AUTO_INCREMENT," +
                        "  `right` VARCHAR(100) NOT NULL," +
                        "  PRIMARY KEY (`id`)," +
                        "  UNIQUE INDEX `id_UNIQUE` (`id` ASC)," +
                        "  UNIQUE INDEX `right_UNIQUE` (`right` ASC));";

            case ROLE_RIGHT:
                return "  CREATE TABLE IF NOT EXISTS role_right (" +
                        "  id INT NOT NULL AUTO_INCREMENT,"+
                        "  role_id INT NOT NULL," +
                        "  right_id INT NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  INDEX role_id_idx (role_id ASC)," +
                        "  INDEX right_id_idx (right_id ASC)," +
                        "  CONSTRAINT role_id" +
                        "    FOREIGN KEY (role_id)" +
                        "    REFERENCES role (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE," +
                        "  CONSTRAINT right_id" +
                        "    FOREIGN KEY (right_id)" +
                        "    REFERENCES `right` (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE);";
            case ACCOUNT:
                return "CREATE TABLE IF NOT EXISTS account (" +
                        "  id INT NOT NULL AUTO_INCREMENT," +
                        "  type VARCHAR(200) NOT NULL," +
                        "  balance INT NOT NULL," +
                        "  client_id INT NOT NULL,"+
                        "  creation DATE NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  CONSTRAINT client_id" +
                        "    FOREIGN KEY (client_id)" +
                        "    REFERENCES `client` (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE);";
            case ACTIVITY:
                return "CREATE TABLE IF NOT EXISTS activity (" +
                        "  id INT NOT NULL AUTO_INCREMENT," +
                        "  type VARCHAR(200) NOT NULL," +
                        "  us_id INT NOT NULL," +
                        "  date DATE NOT NULL," +
                        "  cl_id INT NULL,"+
                        "  acc_id INT NULL,"+
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  CONSTRAINT cl_id" +
                        "    FOREIGN KEY (cl_id)" +
                        "    REFERENCES `client` (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE," +
                        "  CONSTRAINT us_id" +
                        "    FOREIGN KEY (us_id)" +
                        "    REFERENCES `user` (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE," +
                        "  CONSTRAINT acc_id" +
                        "    FOREIGN KEY (acc_id)" +
                        "    REFERENCES `account` (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE);";
            case USER_ROLE:
                return "\tCREATE TABLE IF NOT EXISTS user_role (" +
                        "  id INT NOT NULL AUTO_INCREMENT," +
                        "  user_id INT NOT NULL," +
                        "  role_id INT NOT NULL," +
                        "  PRIMARY KEY (id)," +
                        "  UNIQUE INDEX id_UNIQUE (id ASC)," +
                        "  INDEX user_id_idx (user_id ASC)," +
                        "  INDEX role_id_idx (role_id ASC)," +
                        "  CONSTRAINT user_fkid" +
                        "    FOREIGN KEY (user_id)" +
                        "    REFERENCES user (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE," +
                        "  CONSTRAINT role_fkid" +
                        "    FOREIGN KEY (role_id)" +
                        "    REFERENCES role (id)" +
                        "    ON DELETE CASCADE" +
                        "    ON UPDATE CASCADE);";


            default:
                return "";

        }
    }

}
