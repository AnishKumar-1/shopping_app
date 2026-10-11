resource "aws_secretsmanager_secret" "jwt_secret" {
  name        = "shopping-app/jwt-secret"
  description = "JWT signing secret shared by authentication service and gateway"

  tags = {
    Name = "shopping-app-jwt-secret"
  }
}

# Private ECS tasks use this VPC endpoint to reach AWS Secrets Manager
resource "aws_vpc_endpoint" "secrets_manager" {
  vpc_id = aws_vpc.main.id

  service_name = "com.amazonaws.ap-south-1.secretsmanager"

  vpc_endpoint_type = "Interface"

  subnet_ids = [
    aws_subnet.private.id
  ]

  security_group_ids = [
    aws_security_group.vpc_endpoint.id
  ]

  private_dns_enabled = true

  tags = {
    Name = "shopping-app-secrets-manager-endpoint"
  }
}