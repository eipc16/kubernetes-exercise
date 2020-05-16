import React from 'react';
import {Card, Col, Row} from "antd";
import {MovieList} from "../../models/movies-list";


export const MovieListComponent = (props: MovieList) => {
    const {list} = props;
    return (
        <Row>
            {list && list.map(item =>
                <Col key={item.imdbId} span={6}>
                    <Card>
                        <img src={item.imdbId} alt={"poster"}/>
                    </Card>
                </Col>)
            }
        </Row>
    )
};
