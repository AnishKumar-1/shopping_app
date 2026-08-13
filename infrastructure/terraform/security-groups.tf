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

//allow traffic from ALB to ECS only the route
resource "aws_vpc_security_group_ingress_rule" "ecs_from_alb" {
  ip_protocol                  = "tcp"
  security_group_id            = aws_security_group.ecs.id
  referenced_security_group_id = aws_security_group.alb.id
  from_port                    = 9090
  to_port                      = 9090
  description                  = "Allow traffic from ALB to ECS Gateway"
}
