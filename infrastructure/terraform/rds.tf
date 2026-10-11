resource "aws_db_subnet_group" "mysql" {
  name = "shopping-mysql-subnet-group"
  subnet_ids = [
    aws_subnet.private.id,
    aws_subnet.private_2.id
  ]
  tags = {
    name = "shopping-mysql-subnet-group"
  }
}


#rds security group
resource "aws_security_group" "rds" {
  name        = "shopping-rds-group"
  description = "Security group for shopping mysql database"
  vpc_id      = aws_vpc.main.id
  tags = {
    name = "shopping-rds-sg"
  }
}

#defining ingress rule to handle incoming request on my db who is allowed to access or not on which port
resource "aws_vpc_security_group_ingress_rule" "rds_mysql_from_ecs" {
  ip_protocol       = "tcp"
  security_group_id = aws_security_group.rds.id
  #Allow resources that have the ECS security group to connect to this RDS security group.
  referenced_security_group_id = aws_security_group.ecs.id
  from_port                    = 3306
  to_port                      = 3306
}

#Allow RDS outbound traffic
#Outbound → where RDS can send traffic
resource "aws_vpc_security_group_egress_rule" "rds_all_outbound" {
  ip_protocol       = "-1"
  security_group_id = aws_security_group.rds.id
  cidr_ipv4         = "0.0.0.0/0"
  description       = "allowed rds outbound traffic"
}

#Create MySQL RDS
resource "aws_db_instance" "mysql" {
  identifier = "shopping-mysql"
  #database engine
  engine = "mysql"
  #mysql 8
  engine_version = "8.0"
  # Small instance for our learning/project environment
  instance_class = "db.t3.micro"
  #storage
  allocated_storage = 20
  storage_type      = "gp3"
  # Database username
  username = "admin"
  # We'll handle the password separately
  manage_master_user_password = true
  # Put RDS inside our private subnets
  db_subnet_group_name = aws_db_subnet_group.mysql.name
  # Only ECS can connect to MySQL
  vpc_security_group_ids = [
    aws_security_group.rds.id
  ]
  # Only ECS can connect to MySQL
  publicly_accessible = false
  # We are not using Multi-AZ for this project yet
  multi_az = false
  # Don't automatically create backups for this learning setup
  backup_retention_period = 0
  # Don't upgrade the DB version automatically
  auto_minor_version_upgrade = false
  # Don't delete the database unexpectedly
  deletion_protection = false
  # Don't take a final snapshot when Terraform destroys it
  skip_final_snapshot = true
  tags = {
    name = "shopping-mysql"
  }
}