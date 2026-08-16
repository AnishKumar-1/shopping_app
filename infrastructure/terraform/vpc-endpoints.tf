# Private VPC endpoint for the Amazon ECR API.
# Allows ECS tasks in the private subnet to communicate
# with ECR without using a NAT Gateway.
resource "aws_vpc_endpoint" "ecr_api" {
  vpc_id = aws_vpc.main.id
  # AWS ECR API endpoint for ap-south-1
  service_name = "com.amazonaws.ap-south-1.ecr.api"
  # Interface endpoint creates private network interfaces
  # inside the selected subnet(s).
  vpc_endpoint_type = "Interface"
  # Put the endpoint inside our private subnet.
  subnet_ids = [
    aws_subnet.private.id
  ]
  # Use the security group we just created.
  security_group_ids = [
    aws_security_group.vpc_endpoint.id
  ]
  # Allows the normal AWS ECR hostname to resolve
  # to the private endpoint IP.
  private_dns_enabled = true
  tags = {
    Name = "shopping-app-ecr-api-endpoint"
  }
}


# Private VPC endpoint for the ECR Docker Registry.
# ECS uses this endpoint when communicating with the
# Docker registry to pull the container image.
resource "aws_vpc_endpoint" "ecr_dkr" {
  vpc_id = aws_vpc.main.id
  # ECR Docker Registry endpoint for ap-south-1
  service_name = "com.amazonaws.ap-south-1.ecr.dkr"
  # Interface endpoint creates a private ENI
  # inside the private subnet.
  vpc_endpoint_type = "Interface"
  # Put the endpoint where our ECS tasks are running.
  subnet_ids = [
    aws_subnet.private.id
  ]
  # Allow ECS tasks to connect to this endpoint.
  security_group_ids = [
    aws_security_group.vpc_endpoint.id
  ]
  # Resolve the normal ECR hostname privately.
  private_dns_enabled = true
  tags = {
    Name = "shopping-app-ecr-dkr-endpoint"
  }
}

# Private Gateway VPC Endpoint for Amazon S3.
#
# ECR uses S3 to store Docker image layers.
# This allows the ECS task in the private subnet
# to access S3 without going through a NAT Gateway.
resource "aws_vpc_endpoint" "s3" {

  # VPC where the endpoint will exist
  vpc_id = aws_vpc.main.id

  # S3 service in the ap-south-1 region
  service_name = "com.amazonaws.ap-south-1.s3"

  # S3 supports Gateway endpoints.
  # Unlike Interface endpoints, this does NOT create
  # an ENI in our subnet.
  vpc_endpoint_type = "Gateway"

  # Associate the endpoint with the private route table.
  # Routes for S3 traffic will be added to this route table.
  route_table_ids = [
    aws_route_table.private.id
  ]

  tags = {
    Name = "shopping-app-s3-endpoint"
  }
}


# Private Interface VPC Endpoint for CloudWatch Logs.
# Allows ECS tasks in the private subnet to send
# container logs to CloudWatch without using NAT.

resource "aws_vpc_endpoint" "cloudwatch_logs" {

  # VPC where the endpoint will be created
  vpc_id = aws_vpc.main.id

  # CloudWatch Logs service in ap-south-1
  service_name = "com.amazonaws.ap-south-1.logs"

  # Interface endpoint creates a private ENI
  vpc_endpoint_type = "Interface"

  # Put the endpoint in the private subnet
  subnet_ids = [
    aws_subnet.private.id
  ]

  # Allow ECS tasks to communicate with the endpoint
  security_group_ids = [
    aws_security_group.vpc_endpoint.id
  ]

  # Resolve CloudWatch Logs hostname to the private endpoint
  private_dns_enabled = true

  tags = {
    Name = "shopping-app-cloudwatch-logs-endpoint"
  }
}
