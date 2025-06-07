SELECT SUM(
			(dv.precio_unitario * dv.cantidad) * (i.valor::numeric/100)/(1+(i.valor::numeric/100))
		  ) AS total_iva 
                        FROM detalle_ventas dv 
                        LEFT JOIN producto p ON dv.producto_id = p.id 
                        LEFT JOIN iva i ON p.idiva = i.id 
                        WHERE dv.venta_id = 2;