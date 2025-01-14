CREATE TABLE task_entity
(
    task_id     INT NOT NULL AUTO_INCREMENT,
    description TEXT,
    completed   BOOLEAN   DEFAULT FALSE,
    priority    ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL DEFAULT 'LOW',
    created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (task_id)
) ENGINE=INNODB;

CREATE TABLE sub_task_entity
(
    sub_task_id INT AUTO_INCREMENT,
    description TEXT,
    completed   BOOLEAN DEFAULT FALSE,
    priority    INT,
    task_id     INT,
    PRIMARY KEY (sub_task_id),
    FOREIGN KEY (task_id) REFERENCES task_entity (task_id)
        ON DELETE CASCADE
) ENGINE=INNODB;