moved {
  from = aws_subnet.private_1
  to   = aws_subnet.private
}

moved {
  from = aws_subnet.public_1
  to   = aws_subnet.public
}

moved {
  from = aws_route_table_association.private_1
  to   = aws_route_table_association.private
}

moved {
  from = aws_route_table_association.public_1
  to   = aws_route_table_association.public
}