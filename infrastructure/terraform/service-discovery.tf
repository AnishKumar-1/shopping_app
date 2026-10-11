# Private DNS namespace for services inside our VPC.
resource "aws_service_discovery_private_dns_namespace" "main" {
  name        = "shopping.local"
  vpc         = aws_vpc.main.id
  description = "Private service discovery namespace for shopping app"

  tags = {
    Name = "shopping-service-discovery"
  }
}

# Cloud Map service for Eureka.
# This creates the private DNS name:
#
# eureka.shopping.local
#
# ECS Eureka tasks will register themselves here.
resource "aws_service_discovery_service" "eureka" {
  name = "eureka"
  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.main.id
    dns_records {
      ttl  = 10
      type = "A"
    }
    routing_policy = "MULTIVALUE"
  }
  tags = {
    Name = "shopping-eureka-discovery"
  }

}
