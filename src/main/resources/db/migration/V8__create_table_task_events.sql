CREATE TABLE task_events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT,
    occurrence datetime,
    name varchar(30)
)