//ALB security group
resource "aws_security_group" "alb" {
  name        = "shopping-application-alb-sg"
  description = "Security group for Shopping alb group"
  vpc_id      = aws_vpc.main.id
  tags = {
    Name = "shopping-group-alb-sg"
  }
}

//ecs security group
resource "aws_security_group" "ecs" {
  name        = "shopping-application-ecs-sg"
  description = "Security group for shopping application ECS services"
  vpc_id      = aws_vpc.main.id
  tags = {
    Name = "shopping-application-ecs-sg"
  }
}

//The ALB's Security Group is allowed to accept incoming TCP requests on port 80 from anywhere on the internet.
resource "aws_vpc_security_group_ingress_rule" "alb_http" {
  security_group_id = aws_security_group.alb.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "tcp"
  from_port         = 80
  to_port           = 80
  description       = "Allow HTTP traffic from the internet"
}



# Allows ECS tasks to make outbound connections.
# This is required for things such as:
# - ECR
# - CloudWatch Logs
# - External APIs
# - Other services that the application needs to call
resource "aws_vpc_security_group_egress_rule" "ecs_all_outbound" {
  security_group_id = aws_security_group.ecs.id
  # Allow all outbound traffic from ECS tasks.
  cidr_ipv4   = "0.0.0.0/0"
  ip_protocol = "-1"
  description = "Allow ECS tasks to make outbound connections"
}
# Security Group for Interface VPC Endpoints.
# The endpoint provides private access to AWS services such as ECR.
resource "aws_security_group" "vpc_endpoint" {
  name        = "shopping-application-vpc-endpoint-sg"
  description = "Security group for VPC Interface Endpoints"
  vpc_id      = aws_vpc.main.id
  tags = {
    Name = "shopping-application-vpc-endpoint-sg"
  }
}

# Allows ECS tasks to connect to the VPC Interface Endpoint using HTTPS.
resource "aws_vpc_security_group_ingress_rule" "vpc_endpoint_https" {
  # The security group attached to the VPC Endpoint
  security_group_id = aws_security_group.vpc_endpoint.id
  # Only ECS tasks are allowed to connect
  referenced_security_group_id = aws_security_group.ecs.id
  # HTTPS
  ip_protocol = "tcp"
  # HTTPS port
  from_port   = 443
  to_port     = 443
  description = "Allow HTTPS from ECS tasks to VPC Endpoint"
}

# Allow the ALB to make outbound connections.
resource "aws_vpc_security_group_egress_rule" "alb_outbound" {
  security_group_id = aws_security_group.alb.id
  cidr_ipv4         = "0.0.0.0/0"
  ip_protocol       = "-1"
  description       = "Allow ALB outbound traffic"
}

# Allow the ALB to access Eureka on port 8761.
resource "aws_vpc_security_group_ingress_rule" "eureka_from_alb" {
  ip_protocol                  = "tcp"
  security_group_id            = aws_security_group.ecs.id
  referenced_security_group_id = aws_security_group.alb.id
  from_port                    = 8761
  to_port                      = 8761
  description                  = "Allow ALB to access Eureka"
}

# //allow traffic from ALB to ECS only the route
# resource "aws_vpc_security_group_ingress_rule" "ecs_from_alb" {
#   ip_protocol                  = "tcp"
#   security_group_id            = aws_security_group.ecs.id
#   referenced_security_group_id = aws_security_group.alb.id
#   from_port                    = 9090
#   to_port                      = 9090
#   description                  = "Allow traffic from ALB to ECS Gateway"
# }