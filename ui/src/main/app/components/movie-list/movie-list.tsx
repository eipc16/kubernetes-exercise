import React from 'react';
import {Card, Col, Row} from "antd";
import {MovieList} from "../../models/movies-list";

type MovieListComponentProps = MovieList

export const MovieListComponent = (props: MovieListComponentProps) => {
    return (
        <Row>
            {props.movieList.map(item =>
                <Col key={item.imdbId} span={6}>
                    <Card>
                        <img src={item.imdbId} alt={"poster"}/>
                    </Card>
                </Col>)
            }
        </Row>
    )
};