--Réinitialisation
-- Drop the tables in the correct order to handle dependencies
DROP TABLE IF EXISTS client_vehicule CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS vehicule CASCADE;
DROP TABLE IF EXISTS Borne CASCADE;
DROP TABLE IF EXISTS Reservation CASCADE;

-- Drop the domain
DROP DOMAIN IF EXISTS enum_etat_borne;


--Création des tables
CREATE TABLE client (
    id_client SERIAL PRIMARY KEY,
    nom_client VARCHAR(50) NOT NULL,
    prenom_client VARCHAR(50) NOT NULL,
    adresse_client VARCHAR(255) NOT NULL UNIQUE,
    password_client VARCHAR(255) NOT NULL,
    num_tel_client VARCHAR(15)
);

CREATE TABLE vehicule (
	plaque_vehicule VARCHAR(9) NOT NULL PRIMARY KEY
);

CREATE TABLE client_vehicule (
    id_client INTEGER NOT NULL,
    plaque_vehicule VARCHAR(9) NOT NULL,
    PRIMARY KEY (id_client, plaque_vehicule),
    FOREIGN KEY (id_client) REFERENCES client(id_client) ON DELETE CASCADE,
    FOREIGN KEY (plaque_vehicule) REFERENCES vehicule(plaque_vehicule) ON DELETE CASCADE
);

CREATE DOMAIN enum_etat_borne VARCHAR(50) NOT NULL CHECK
    (VALUE IN ('disponible', 'reservé', 'indisponible'));


CREATE TABLE Borne (
    id_borne SERIAL PRIMARY KEY,
    etat_borne enum_etat_borne NOT NULL DEFAULT 'indisponible'
);

INSERT INTO Borne(etat_borne) VALUES ('disponible'),
                                     ('disponible'),
                                     ('disponible');
CREATE TABLE Reservation (
    num_reservation SERIAL PRIMARY KEY,
    id_borne INTEGER NOT NULL,
    plaque_vehicule VARCHAR(9) NOT NULL,
    date_debut_reservation TIMESTAMP NOT NULL,
    date_fin_reservation TIMESTAMP NOT NULL,
    FOREIGN KEY (id_borne) REFERENCES Borne(id_borne) ON DELETE CASCADE,
    FOREIGN KEY (plaque_vehicule) REFERENCES vehicule(plaque_vehicule) ON DELETE CASCADE
);
